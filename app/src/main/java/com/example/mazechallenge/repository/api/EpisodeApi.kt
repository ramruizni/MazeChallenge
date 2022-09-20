package com.example.mazechallenge.repository.api

import com.example.mazechallenge.model.EpisodeListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EpisodeApi {

    @GET("shows/{showId}?embed=episodes")
    suspend fun fetchEpisodeListByShowId(
        @Path("showId") showId: Int
    ): Response<EpisodeListResponse>
}