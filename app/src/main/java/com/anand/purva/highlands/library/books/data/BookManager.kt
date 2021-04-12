package com.anand.purva.highlands.library.books.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookManager @Inject constructor(
    private val trie: IBookTrie,
    private val repo: IBookRepository
): IBookManager {
    override fun initialize(): Flow<SearchResult> {
        return flow {
            repo.loadBooks().collect { it ->
                it.forEach { trie.insert(it) }
                emit(SearchResult(it))
            }
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
}