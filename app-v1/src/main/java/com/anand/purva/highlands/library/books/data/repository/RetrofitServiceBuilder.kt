/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */

package com.anand.purva.highlands.library.books.data.repository

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitServiceBuilder @Inject constructor() {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val bookCatalogService: IBookCatalogService by lazy {
        retrofit.create(IBookCatalogService::class.java)
    }

    companion object {
        private const val BASE_URL = "https://anandvnath.github.io/Purva-HighLands-Library/"
    }
}