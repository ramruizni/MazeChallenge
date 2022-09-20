package com.example.mazechallenge.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Schedule(
    val time: String?,
    val days: List<String>?
) : Parcelable