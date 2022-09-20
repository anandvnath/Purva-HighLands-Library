/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */

package com.anand.purva.highlands.library.books.data

internal class BookTrieNode(val character: Char) {
    var left: BookTrieNode? = null
    var right: BookTrieNode? = null
    var middle: BookTrieNode? = null
    val searchResult =
        SearchResult()
}