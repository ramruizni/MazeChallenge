package com.example.mazechallenge.repository

import com.example.mazechallenge.model.Episode
import com.example.mazechallenge.model.EpisodeListResponse
import com.example.mazechallenge.model.Show
import com.example.mazechallenge.repository.api.EpisodeApi
import com.example.mazechallenge.repository.database.dao.ShowDao
import com.example.mazechallenge.testability.DispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EpisodeRepository @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val api: EpisodeApi,
    private val showDao: ShowDao
) {

    suspend fun fetchShowEpisodes(show: Show): List<Episode>? {
        return withContext(dispatchers.io) {
            val response = api.fetchEpisodeListByShowId(show.id).body() as EpisodeListResponse
            show.episodes = response._embedded.episodes
            showDao.update(show)
            return@withContext show.episodes
        }
    }
}