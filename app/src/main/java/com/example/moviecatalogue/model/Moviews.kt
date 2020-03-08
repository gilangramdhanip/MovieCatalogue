package com.example.moviecatalogue.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Moviews(
    var id: Int,
    var popularity: Double,
    var vote_count: Int,
    var poster_path: String,
    var title: String,
    var vote_average: Double,
    var overview: String,
    var release_date: String?,
    var isFav: Boolean = false
) : Parcelable