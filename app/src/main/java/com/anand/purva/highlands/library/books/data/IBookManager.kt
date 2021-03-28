/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */

package com.anand.purva.highlands.library.books.data

import kotlinx.coroutines.CoroutineScope

interface IBookManager {
    val allBooks: SearchResult
    fun initialize(scope: CoroutineScope, callback: (b: Boolean) -> Unit)
    suspend fun search(query: String): SearchResult
}