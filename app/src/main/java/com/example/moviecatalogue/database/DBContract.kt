package com.example.moviecatalogue.database

import android.net.Uri
import android.provider.BaseColumns

object DBContract {

    const val AUTHORITY = "com.example.moviecatalogue"
    const val SCHEME = "content"

    internal class FavoriteColum : BaseColumns{
        companion object{
            const val MOVIE_TABLE = "favorites_movie"
            const val MOVIE_ID = "_id"
            const val MOVIE_POPULARITY = "popularity"
            const val MOVIE_VOTE_COUNT = "vote_count"
            const val MOVIE_POSTER_PATH = "poster_path"
            const val MOVIE_TITLE= "title"
            const val MOVIE_VOTE_AVERAGE= "vote_average"
            const val MOVIE_OVERVIEW = "overview"
            const val MOVIE_RELEASE_DATE = "release_date"

            const val TV_SHOW_TABLE = "favorites_tv"
            const val TV_ID = "_id"
            const val TV_NAME= "name"
            const val TV_POPULARITY= "popularity"
            const val TV_VOTE_COUNT= "vote_count"
            const val TV_FIRST_AIR_DATE= "first_air_date"
            const val TV_VOTE_AVERAGE= "vote_average"
            const val TV_OVERVIEW= "overview"
            const val TV_POSTER_PATH= "poster_path"

            val CONTENT_MOVIE_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(MOVIE_TABLE)
                .build()

            val CONTENT_TV_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TV_SHOW_TABLE)
                .build()
        }
    }
}