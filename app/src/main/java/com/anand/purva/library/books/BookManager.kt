package com.anand.purva.library.books

import android.app.Application
import android.util.Log
import com.opencsv.CSVReader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class BookManager(scope: CoroutineScope, private val application: Application) {
    private val books = mutableListOf<Book>()
    lateinit var  allBooks: SearchResult
    private val trie = BookTrie()

    init {
        scope.launch {
            loadBooks()
            books.forEach { trie.insert(it) }
            allBooks = SearchResult(books)
        }
    }

    suspend fun search(query: String): SearchResult = trie.search(query)

    private fun loadBooks() {
        val bufferedReader = application.assets.open("lib-data.csv").bufferedReader()
        val reader = CSVReader(bufferedReader)
        reader.readAll().forEach { row ->
            books.add(Book(row[0], row[1], row[2]))
        }
        Log.d("LibApp", "Books loaded: ${books.size}")
    }
}

private enum class Mode {
    AUTHOR, TITLE
}

private class BookTrie {
    private var root: BookTrieNode? = null

    fun insert(book: Book) {
        book.title.split(" ").forEach {
            val key = it.toLowerCase()
            val d = findNextAlphabetLocation(key, 0)
            if (d < key.length) {
                root = insert(
                    root,
                    key,
                    book,
                    d,
                    Mode.TITLE
                )
            }
        }
        book.author.split(" ").forEach {
            val key = it.toLowerCase()
            val d = findNextAlphabetLocation(key, 0)
            if (d < key.length) {
                root = insert(
                    root,
                    key,
                    book,
                    d,
                    Mode.AUTHOR
                )
            }
        }
    }

    fun search(query: String): SearchResult {
        val key = query.toLowerCase()
        val k = findNextAlphabetLocation(key, 0)
        val result = search(key, root, k)
        Log.d("LibApp", "Result: ${result.titleBooks.size} and ${result.authorsBooks.size}")
        return result
    }

    private fun search(
        query: String,
        node: BookTrieNode?,
        k: Int
    ): SearchResult {
        if (node == null || k == query.length)
            return SearchResult()

        val currChar = query[k]
        if (currChar > node.character) {
            return search(query, node.right, k)
        } else if (currChar < node.character) {
            return search(query, node.left, k)
        }

        if (k == query.length - 1)
            return node.searchResult
        return search(query, node.middle, findNextAlphabetLocation(query, k + 1))
    }

    private fun insert(
        node: BookTrieNode?,
        key: String,
        book: Book,
        k: Int,
        mode: Mode
    ): BookTrieNode? {
        if (k == key.length) {
            return null
        }
        if (key.startsWith("har")) {
            println("Inserting $key")
        }
        val currChar = key[k]
        var newNode: BookTrieNode? = node
        if (newNode == null) {
            newNode = BookTrieNode(currChar)
        }

        if (currChar < newNode.character)
            newNode.left = insert(newNode.left, key, book, k, mode)
        else if (currChar > newNode.character)
            newNode.right = insert(newNode.right, key, book, k, mode)
        else {
            when (mode) {
                Mode.AUTHOR -> newNode.searchResult.authorsBooks.add(book)
                Mode.TITLE -> newNode.searchResult.titleBooks.add(book)
            }
            if (k < key.length - 1)
                newNode.middle = insert(newNode.middle, key, book, findNextAlphabetLocation(key, k+1), mode)
        }

        return newNode
    }

    private fun findNextAlphabetLocation(word: String, startIndex: Int): Int {
        if (startIndex == word.length)
            return startIndex

        var c = word[startIndex]
        if (c in 'a'..'z')
            return startIndex
        else
            return findNextAlphabetLocation(word, startIndex + 1)
    }

}

class SearchResult(books: List<Book> = emptyList()) {
    val authorsBooks = mutableSetOf<Book>()
    val titleBooks = mutableSetOf<Book>()
    init {
        titleBooks.addAll(books)
    }
    override fun toString(): String {
        return "$authorsBooks $titleBooks"
    }
}

private class BookTrieNode(val character: Char) {
    var left: BookTrieNode? = null
    var right: BookTrieNode? = null
    var middle: BookTrieNode? = null
    val searchResult = SearchResult()
}