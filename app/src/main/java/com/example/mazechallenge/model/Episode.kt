package com.example.mazechallenge.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Episode(
    val id: Int,
    val image: Image?,
    val name: String,
    val number: Int,
    val season: Int,
    val summary: String
): Parcelable