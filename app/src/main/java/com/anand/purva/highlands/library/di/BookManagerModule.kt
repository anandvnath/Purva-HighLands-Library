/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */

package com.anand.purva.highlands.library.di

import com.anand.purva.highlands.library.books.BookManager
import com.anand.purva.highlands.library.books.IBookManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BookManagerModule {
    @Binds
    abstract fun bindBookManager(bookManager: BookManager): IBookManager
}