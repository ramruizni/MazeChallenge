package com.example.mazechallenge.repository.api

import com.example.mazechallenge.model.ShowListPageResponse
import com.example.mazechallenge.model.ShowListQueryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ShowApi {

    @GET("shows")
    suspend fun fetchShowListByPage(
        @Query("page") page: Int
    ): Response<ShowListPageResponse>

    @GET("search/shows")
    suspend fun queryShowListByName(
        @Query("q") name: String
    ): Response<ShowListQueryResponse>
}