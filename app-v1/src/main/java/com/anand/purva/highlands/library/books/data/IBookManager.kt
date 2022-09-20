package com.anand.purva.highlands.library.books.data

import kotlinx.coroutines.flow.Flow

interface IBookManager {
    fun initialize(): Flow<SearchResult>
    fun search(query: String): SearchResult
}