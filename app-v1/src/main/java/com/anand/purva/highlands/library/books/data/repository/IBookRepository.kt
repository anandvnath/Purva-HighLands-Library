/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */

package com.anand.purva.highlands.library.books.data.repository

import com.anand.purva.highlands.library.books.data.Book
import kotlinx.coroutines.flow.Flow

interface IBookRepository {
    fun loadBooks(): Flow<List<Book>>
}