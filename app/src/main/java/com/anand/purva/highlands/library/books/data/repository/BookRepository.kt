/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */

package com.anand.purva.highlands.library.books.data.repository

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.anand.purva.highlands.library.R
import com.anand.purva.highlands.library.books.data.Book
import de.siegmar.fastcsv.reader.CsvReader
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    private val application: Application,
    private val retrofitServiceBuilder: RetrofitServiceBuilder): IBookRepository {
    override fun loadBooks(): Flow<List<Book>> {
        return flow {
            val sharedPref = application.getSharedPreferences(application.getString(R.string.pref_name), Context.MODE_PRIVATE)
            sharedPref.getString(DOWNLOADED_CATALOG_PATH, null)?.let { downloadPath ->
                Log.d(TAG, "Found an existing catalog download at $downloadPath. Proceeding to check time since last sync.")
                val downloadedCatalogTime = sharedPref.getLong(DOWNLOADED_CATALOG_TIME, 0)
                if ((System.currentTimeMillis() - downloadedCatalogTime) > DAY_IN_MILLIS) {
                    Log.d(TAG, "Time since last sync exceeded 24hrs. Loading from network.")
                    loadBooksFromNetwork(sharedPref, downloadPath)?.let { emit(it) }
                } else {
                    Log.d(TAG, "Time since last sync still under 24hrs. Returning downloaded catalog from $downloadPath.")
                    emit(loadBooksFromDownload(downloadPath))
                }
                return@flow
            }

            Log.d(TAG, "No previously saved catalog found. Serving from packaged catalog.")
            emit(loadBooksFromAssets())
            Log.d(TAG, "Proceeding to load catalog from network.")
            loadBooksFromNetwork(sharedPref, null)?.let { emit(it) }
        }
    }

    private suspend fun loadBooksFromNetwork(sharedPref: SharedPreferences, downloadedCatalogPath: String?): List<Book>? {
        Log.d(TAG, "Downloading latest catalog...")
        val downloadedCatalogVersion = sharedPref.getString(DOWNLOADED_CATALOG_VERSION, null)
        val latestCatalogVersion = retrofitServiceBuilder.bookCatalogService.getLatestVersion()

        if (latestCatalogVersion.version != downloadedCatalogVersion) {
            Log.d(TAG, "Newer version of catalog '${latestCatalogVersion.version}' available. Existing '$downloadedCatalogVersion'")
            val responseBody = retrofitServiceBuilder.bookCatalogService.downloadFile(latestCatalogVersion.version).body()
            val fileName = application.filesDir.absolutePath + File.separator + BOOK_CSV_STORE
            saveFile(responseBody, fileName)?.let { file ->
                Log.d(TAG, "Download successful to file $file")
                with(sharedPref.edit()) {
                    putLong(DOWNLOADED_CATALOG_TIME, System.currentTimeMillis())
                    putString(DOWNLOADED_CATALOG_VERSION, latestCatalogVersion.version)
                    putString(DOWNLOADED_CATALOG_PATH, file)
                    apply()
                }
                return loadBooksFromDownload(file)
            }
        }

        return downloadedCatalogPath?.let { path ->
            Log.d(TAG, "New version download may have been unsuccessful.")
            Log.d(TAG, "Processing version of catalog '${latestCatalogVersion.version}' that is already available.")
            loadBooksFromDownload(path)
        }
    }

    private fun loadBooksFromDownload(downloadedCatalogPath: String) =
        loadBooksFromBufferedReader(File(downloadedCatalogPath).bufferedReader())

    private fun loadBooksFromAssets() =
        loadBooksFromBufferedReader(application.assets.open(BOOK_CSV_STORE).bufferedReader())


    private fun loadBooksFromBufferedReader(bufferedReader: BufferedReader): MutableList<Book> {
        val books = mutableListOf<Book>()
        CsvReader.builder().build(bufferedReader).forEach { row ->
            books.add(
                Book(
                    row.getField(ID_INDEX).trim(),
                    row.getField(TITLE_INDEX).trim(),
                    row.getField(AUTHOR_INDEX).trim(),
                    row.getField(CATEGORY_INDEX).trim()
                )
            )
        }

        Log.d(TAG, "Books loaded: ${books.size}")
        return books
    }

    private fun saveFile(body: ResponseBody?, filePath: String): String? {
        if (body == null) return null
        val catalogFile = File(filePath)
        if (catalogFile.exists()) {
            Log.d(TAG, "Found existing catalog file, deleting..")
            catalogFile.delete()
        }
        var input: InputStream? = null

        try {
            input = body.byteStream()
            val fos = FileOutputStream(filePath)
            fos.use { output ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
            return filePath
        } catch (e:Exception) {
            Log.e(TAG, e.toString())
        } finally {
            input?.close()
        }
        return null
    }

    companion object {
        val TAG = BookRepository::class.simpleName
        const val DOWNLOADED_CATALOG_VERSION = "DownloadedCatalogVersion"
        const val DOWNLOADED_CATALOG_TIME = "DownloadedCatalogTime"
        const val DOWNLOADED_CATALOG_PATH = "DownloadedCatalogPath"
        const val DAY_IN_MILLIS = 24 * 60 * 60 * 1000
        const val BOOK_CSV_STORE = "LibCatMaster.csv"
        const val ID_INDEX = 0
        const val TITLE_INDEX = 1
        const val AUTHOR_INDEX = 2
        const val CATEGORY_INDEX = 3
    }
}