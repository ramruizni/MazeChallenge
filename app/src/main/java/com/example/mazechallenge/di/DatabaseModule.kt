package com.example.mazechallenge.di

import android.content.Context
import androidx.room.Room
import com.example.mazechallenge.common.Constants.DB_NAME
import com.example.mazechallenge.repository.database.ShowDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        ShowDatabase::class.java,
        DB_NAME
    ).build()

    @Provides
    @Singleton
    fun provideEntryDao(db: ShowDatabase) = db.getShowDao()
}