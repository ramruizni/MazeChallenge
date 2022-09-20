package com.example.mazechallenge.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    val medium: String?,
    val original: String?
): Parcelable