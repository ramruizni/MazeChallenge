package com.example.mazechallenge.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mazechallenge.model.Show
import com.example.mazechallenge.repository.database.dao.ShowDao

@Database(
    entities = [Show::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ShowDatabase : RoomDatabase() {

    abstract fun getShowDao(): ShowDao
}