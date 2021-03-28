/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */

package com.anand.purva.highlands.library.books.data

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
