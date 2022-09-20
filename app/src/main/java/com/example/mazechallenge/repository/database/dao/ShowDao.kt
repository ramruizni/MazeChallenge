package com.example.mazechallenge.repository.database.dao

import androidx.room.*
import com.example.mazechallenge.model.Show

@Dao
interface ShowDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(list: List<Show>)

    @Query("SELECT * FROM show")
    suspend fun findAll(): List<Show>

    @Query("SELECT * FROM show WHERE isFavorite")
    suspend fun findAllFavorites(): List<Show>

    @Update
    suspend fun update(show: Show)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(show: Show)
}