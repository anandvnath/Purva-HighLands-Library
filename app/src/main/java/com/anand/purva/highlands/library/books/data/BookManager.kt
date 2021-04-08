package com.anand.purva.highlands.library.books.data

import android.app.Application
import android.util.Log
import de.siegmar.fastcsv.reader.CsvReader
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookManager @Inject constructor(
    private val application: Application,
    private val trie: IBookTrie
): IBookManager {
    private val books = mutableListOf<Book>()

    override fun initialize(): Flow<SearchResult> {
        return flow {
            loadBooks()
            books.forEach { trie.insert(it) }
            emit(SearchResult(books))
        }
    }

    override fun search(query: String): SearchResult {
        val results = mutableListOf<SearchResult>()
        query.trim().split(" ").forEach {
            results.add(trie.search(it))
        }
        return mergeResults(results)
    }

    private fun mergeResults(results: List<SearchResult>): SearchResult {
        val result = SearchResult()
        result.authorsBooks.addAll(results[0].authorsBooks)
        result.categoryBooks.addAll(results[0].categoryBooks)
        result.titleBooks.addAll(results[0].titleBooks)
        results.forEach {
            var books = result.authorsBooks.intersect(it.authorsBooks)
            result.authorsBooks.clear()
            result.authorsBooks.addAll(books)

            books = result.titleBooks.intersect(it.titleBooks)
            result.titleBooks.clear()
            result.titleBooks.addAll(books)

            books = result.categoryBooks.intersect(it.categoryBooks)
            result.categoryBooks.clear()
            result.categoryBooks.addAll(books)
        }
        return result
    }

    private fun loadBooks() {
        val bufferedReader = application.assets.open(BOOK_CSV_STORE).bufferedReader()
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
    }

    companion object {
        const val BOOK_CSV_STORE = "LibCatMaster-v0.0.1.csv"
        const val ID_INDEX = 0
        const val TITLE_INDEX = 1
        const val AUTHOR_INDEX = 2
        const val CATEGORY_INDEX = 3
    }
}