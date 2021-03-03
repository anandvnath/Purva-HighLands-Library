package com.anand.purva.library.books

import android.app.Application
import android.util.Log
import de.siegmar.fastcsv.reader.CsvReader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BookManager(
    scope: CoroutineScope,
    private val application: Application,
    callback: (b: Boolean) -> Unit
) {
    private val books = mutableListOf<Book>()
    lateinit var allBooks: SearchResult
    private val trie = BookTrie()

    companion object {
        const val BOOK_CSV_STORE = "LibCatMaster-v0.01.csv"
        const val ID_INDEX = 0
        const val TITLE_INDEX = 1
        const val AUTHOR_INDEX = 2
        const val CATEGORY_INDEX = 3
    }

    init {
        scope.launch(Dispatchers.Main) {
            loadBooks()
            books.forEach { trie.insert(it) }
            allBooks = SearchResult(books)
            callback(true)
        }
    }

    suspend fun search(query: String): SearchResult = trie.search(query)

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
}

private enum class Mode {
    AUTHOR, TITLE, CATEGORY
}

private class BookTrie {
    private var root: BookTrieNode? = null

    fun insert(book: Book) {
        insert(book.title, book, Mode.TITLE)
        insert(book.author, book, Mode.AUTHOR)
        insert(book.category, book, Mode.CATEGORY)
    }

    private fun insert(subject: String, book: Book, mode: Mode) {
        subject.split(" ").forEach {
            val key = it.trim().toLowerCase()
            val d = findNextAlphabetLocation(key, 0)
            if (d < key.length) {
                root = insert(
                    root,
                    key,
                    book,
                    d,
                    mode
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
            return node
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
            insertBookNodeSearchResult(mode, newNode, book)
            if (k < key.length - 1)
                newNode.middle =
                    insert(newNode.middle, key, book, findNextAlphabetLocation(key, k + 1), mode)
        }

        return newNode
    }

    private fun insertBookNodeSearchResult(
        mode: Mode,
        newNode: BookTrieNode,
        book: Book
    ) {
        when (mode) {
            Mode.AUTHOR -> newNode.searchResult.authorsBooks.add(book)
            Mode.TITLE -> newNode.searchResult.titleBooks.add(book)
            Mode.CATEGORY -> newNode.searchResult.categoryBooks.add(book)
        }
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
    val categoryBooks = mutableSetOf<Book>()

    init {
        titleBooks.addAll(books)
        authorsBooks.addAll(books)
        categoryBooks.addAll(books)
    }

    override fun toString(): String {
        return "Author books: ${authorsBooks.size} Title books: ${titleBooks.size} Category Books: ${categoryBooks.size}"
    }
}

private class BookTrieNode(val character: Char) {
    var left: BookTrieNode? = null
    var right: BookTrieNode? = null
    var middle: BookTrieNode? = null
    val searchResult = SearchResult()
}