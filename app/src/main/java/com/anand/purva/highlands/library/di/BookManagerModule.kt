/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */

package com.anand.purva.highlands.library.di

import com.anand.purva.highlands.library.books.data.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BookManagerModule {
    @Binds
    abstract fun bindBookRepository(bookRepository: BookRepository): IBookRepository
    @Binds
    abstract fun bindBookManager(bookManager: BookManager): IBookManager
    @Binds
    abstract fun bindBookTrie(trie: BookTrie): IBookTrie
}