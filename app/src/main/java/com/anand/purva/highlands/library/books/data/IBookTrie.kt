/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */

package com.anand.purva.highlands.library.books.data

interface IBookTrie {
    fun insert(book: Book)
    fun search(query: String): SearchResult
    fun clear()
}