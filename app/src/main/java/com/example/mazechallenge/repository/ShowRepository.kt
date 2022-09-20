package com.example.mazechallenge.repository

import com.example.mazechallenge.model.Show
import com.example.mazechallenge.model.ShowListPageResponse
import com.example.mazechallenge.model.ShowListQueryResponse
import com.example.mazechallenge.repository.api.ShowApi
import com.example.mazechallenge.repository.database.dao.ShowDao
import com.example.mazechallenge.testability.DispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShowRepository @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val api: ShowApi,
    private val dao: ShowDao
) {

    suspend fun getList() = dao.findAll()

    suspend fun fetchListByName(name: String): List<Show> {
        return withContext(dispatchers.io) {
            if (name.isNotEmpty()) {
                val response = api.queryShowListByName(name).body() as ShowListQueryResponse
                val responseAsList = response.map { it.show }
                dao.insertAll(responseAsList)
                return@withContext responseAsList
            }
            return@withContext emptyList<Show>()
        }
    }

    suspend fun fetchListByPage(page: Int): List<Show> {
        return withContext(dispatchers.io) {
            val response = api.fetchShowListByPage(page).body() as ShowListPageResponse
            dao.insertAll(response)
            return@withContext response
        }
    }

    suspend fun fetchFavoriteList(): List<Show> {
        return withContext(dispatchers.io) {
            return@withContext dao.findAllFavorites()
        }
    }

    suspend fun toggleShowAsFavorite(show: Show) {
        withContext(dispatchers.io) {
            show.isFavorite = !show.isFavorite
            dao.update(show)
        }
    }
}