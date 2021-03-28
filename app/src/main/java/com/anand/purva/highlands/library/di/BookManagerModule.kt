/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */

package com.anand.purva.highlands.library.di

import com.anand.purva.highlands.library.books.data.BookManager
import com.anand.purva.highlands.library.books.data.BookTrie
import com.anand.purva.highlands.library.books.data.IBookManager
import com.anand.purva.highlands.library.books.data.IBookTrie
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BookManagerModule {
    @Binds
    abstract fun bindBookManager(bookManager: BookManager): IBookManager
    @Binds
    abstract fun bindBookTrie(trie: BookTrie): IBookTrie
}