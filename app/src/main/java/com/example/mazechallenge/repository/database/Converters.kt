package com.example.mazechallenge.repository.database

import androidx.room.TypeConverter
import com.example.mazechallenge.model.Episode
import com.example.mazechallenge.model.Image
import com.example.mazechallenge.model.Rating
import com.example.mazechallenge.model.Schedule
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun imageToJson(value: Image?) = if (value == null) null else Gson().toJson(value)

    @TypeConverter
    fun jsonToImage(value: String?) =
        if (value == null) null else Gson().fromJson(value, Image::class.java)

    @TypeConverter
    fun ratingToJson(value: Rating) = Gson().toJson(value)

    @TypeConverter
    fun jsonToRating(value: String) = Gson().fromJson(value, Rating::class.java)

    @TypeConverter
    fun stringListToJson(value: List<String>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToStringList(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()

    @TypeConverter
    fun episodeListToJson(value: List<Episode>?) = if (value == null) null else Gson().toJson(value)

    @TypeConverter
    fun jsonToEpisodeList(value: String?) =
        if (value == null) null else Gson().fromJson(value, Array<Episode>::class.java).toList()

    @TypeConverter
    fun scheduleToJson(value: Schedule?) = if (value == null) null else Gson().toJson(value)

    @TypeConverter
    fun jsonToSchedule(value: String?) =
        if (value == null) null else Gson().fromJson(value, Schedule::class.java)
}