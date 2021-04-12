package com.anand.purva.highlands.library.books.data

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookTrie @Inject constructor(): IBookTrie {
    private var root: BookTrieNode? = null

    override fun insert(book: Book) {
        insert(book.title, book, BookAttribute.TITLE)
        insert(book.author, book, BookAttribute.AUTHOR)
        insert(book.category, book, BookAttribute.CATEGORY)
    }

    override fun search(query: String): SearchResult {
        val key = query.toLowerCase()
        val k = findNextAlphabetLocation(key, 0)
        val result = search(key, root, k)
        Log.d("LibApp", "Result: ${result.titleBooks.size} and ${result.authorsBooks.size}")
        return result
    }

    override fun clear() {
        root = null
    }

    private fun insert(subject: String, book: Book, bookAttribute: BookAttribute) {
        subject.split(" ").forEach {
            val key = it.trim().toLowerCase()
            val d = findNextAlphabetLocation(key, 0)
            if (d < key.length) {
                root = insert(
                    root,
                    key,
                    book,
                    d,
                    bookAttribute
                )
            }
        }
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
        bookAttribute: BookAttribute
    ): BookTrieNode? {
        if (k == key.length) {
            return node
        }
        val currChar = key[k]
        var newNode: BookTrieNode? = node
        if (newNode == null) {
            newNode = BookTrieNode(
                currChar
            )
        }

        if (currChar < newNode.character)
            newNode.left = insert(newNode.left, key, book, k, bookAttribute)
        else if (currChar > newNode.character)
            newNode.right = insert(newNode.right, key, book, k, bookAttribute)
        else {
            insertBookNodeSearchResult(bookAttribute, newNode, book)
            if (k < key.length - 1)
                newNode.middle =
                    insert(newNode.middle, key, book, findNextAlphabetLocation(key, k + 1), bookAttribute)
        }

        return newNode
    }

    private fun insertBookNodeSearchResult(
        bookAttribute: BookAttribute,
        newNode: BookTrieNode,
        book: Book
    ) {
        when (bookAttribute) {
            BookAttribute.AUTHOR -> newNode.searchResult.authorsBooks.add(book)
            BookAttribute.TITLE -> newNode.searchResult.titleBooks.add(book)
            BookAttribute.CATEGORY -> newNode.searchResult.categoryBooks.add(book)
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
