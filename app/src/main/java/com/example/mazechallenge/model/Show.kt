package com.example.mazechallenge.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Show(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val genres: List<String>,
    val image: Image?,
    val name: String,
    val rating: Rating,
    val summary: String,
    val url: String,
    var episodes: List<Episode>? = null,
    var isFavorite: Boolean = false,
    val schedule: Schedule?,
) : Parcelable