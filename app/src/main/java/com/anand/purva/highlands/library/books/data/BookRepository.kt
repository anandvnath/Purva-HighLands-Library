/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */

package com.anand.purva.highlands.library.books.data

import android.app.Application
import android.util.Log
import de.siegmar.fastcsv.reader.CsvReader
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BookRepository @Inject constructor(private val application: Application): IBookRepository {
    override fun loadBooks(): Flow<List<Book>> {
        return flow {
            emit(loadBooksFromAssets())
        }
    }

    private fun loadBooksFromAssets(): List<Book> {
        val bufferedReader = application.assets.open(BOOK_CSV_STORE).bufferedReader()
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
        Log.d("LibApp", "Books loaded: ${books.size}")
        return books
    }

    companion object {
        const val BOOK_CSV_STORE = "LibCatMaster.csv"
        const val ID_INDEX = 0
        const val TITLE_INDEX = 1
        const val AUTHOR_INDEX = 2
        const val CATEGORY_INDEX = 3
    }
}