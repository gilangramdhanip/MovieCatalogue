package com.example.favoriteapps.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class TvShow(
    var id: Int,
    var name: String,
    var popularity: Double,
    var vote_count: Int,
    var first_air_date: String,
    var vote_average: Double,
    var overview: String,
    var poster_path: String,
    var isFav: Boolean = false
) : Parcelable