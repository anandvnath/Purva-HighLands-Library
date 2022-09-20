/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */

package com.anand.purva.highlands.library.books.data.repository

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface IBookCatalogService {
    @GET("latest-version.json")
    suspend fun getLatestVersion(): LatestCatalog

    @Streaming
    @GET
    suspend fun downloadFile(@Url fileUrl: String): Response<ResponseBody>
}