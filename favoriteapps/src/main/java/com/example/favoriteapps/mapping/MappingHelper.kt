package com.example.favoriteapps.mapping

import android.database.Cursor
import com.example.favoriteapps.database.DBContract.FavoriteColum.Companion.MOVIE_ID
import com.example.favoriteapps.database.DBContract.FavoriteColum.Companion.MOVIE_OVERVIEW
import com.example.favoriteapps.database.DBContract.FavoriteColum.Companion.MOVIE_POPULARITY
import com.example.favoriteapps.database.DBContract.FavoriteColum.Companion.MOVIE_POSTER_PATH
import com.example.favoriteapps.database.DBContract.FavoriteColum.Companion.MOVIE_RELEASE_DATE
import com.example.favoriteapps.database.DBContract.FavoriteColum.Companion.MOVIE_TITLE
import com.example.favoriteapps.database.DBContract.FavoriteColum.Companion.MOVIE_VOTE_AVERAGE
import com.example.favoriteapps.database.DBContract.FavoriteColum.Companion.MOVIE_VOTE_COUNT
import com.example.favoriteapps.database.DBContract.FavoriteColum.Companion.TV_FIRST_AIR_DATE
import com.example.favoriteapps.database.DBContract.FavoriteColum.Companion.TV_ID
import com.example.favoriteapps.database.DBContract.FavoriteColum.Companion.TV_NAME
import com.example.favoriteapps.database.DBContract.FavoriteColum.Companion.TV_OVERVIEW
import com.example.favoriteapps.database.DBContract.FavoriteColum.Companion.TV_POPULARITY
import com.example.favoriteapps.database.DBContract.FavoriteColum.Companion.TV_POSTER_PATH
import com.example.favoriteapps.database.DBContract.FavoriteColum.Companion.TV_VOTE_AVERAGE
import com.example.favoriteapps.database.DBContract.FavoriteColum.Companion.TV_VOTE_COUNT
import com.example.favoriteapps.model.Moviews
import com.example.favoriteapps.model.TvShow

object MappingHelper {

    fun movieMapCursorToArrayList(favCursor: Cursor?): ArrayList<Moviews>{
        val movieFavList = ArrayList<Moviews>()
        favCursor?.apply {
            while (moveToNext()){
                val id = getInt(getColumnIndexOrThrow(MOVIE_ID))
                val popularity = getDouble(getColumnIndexOrThrow(MOVIE_POPULARITY))
                val vote_count = getInt(getColumnIndex(MOVIE_VOTE_COUNT))
                val poster_path = getString(getColumnIndexOrThrow(MOVIE_POSTER_PATH))
                val title = getString(getColumnIndexOrThrow(MOVIE_TITLE))
                val vote_average = getDouble(getColumnIndexOrThrow(MOVIE_VOTE_AVERAGE))
                val overview = getString(getColumnIndexOrThrow(MOVIE_OVERVIEW))
                val release_date= getString(getColumnIndexOrThrow(MOVIE_RELEASE_DATE))
                movieFavList.add(
                    Moviews(
                        id,
                        popularity,
                        vote_count,
                        poster_path,
                        title,
                        vote_average,
                        overview,
                        release_date,
                        true
                    )
                )

            }
        }

        return movieFavList
    }

    fun tvMapCursorToArrayList(favCursor: Cursor?): ArrayList<TvShow>{
        val tvFavList = ArrayList<TvShow>()
        favCursor?.apply {
            while (moveToNext()){
                val id = getInt(getColumnIndexOrThrow(TV_ID))
                val name = getString(getColumnIndexOrThrow(TV_NAME))
                val popularity = getDouble(getColumnIndexOrThrow(TV_POPULARITY))
                val vote_count = getInt(getColumnIndex(TV_VOTE_COUNT))
                val first_air_date= getString(getColumnIndexOrThrow(TV_FIRST_AIR_DATE))
                val vote_average = getDouble(getColumnIndexOrThrow(TV_VOTE_AVERAGE))
                val overview = getString(getColumnIndexOrThrow(TV_OVERVIEW))
                val poster_path = getString(getColumnIndexOrThrow(TV_POSTER_PATH))
                tvFavList.add(
                    TvShow(
                        id,
                        name,
                        popularity,
                        vote_count,
                        first_air_date,
                        vote_average,
                        overview,
                        poster_path,
                        true
                    )
                )
            }
        }

        return tvFavList
    }

//    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<Favorites> {
//        val favList = ArrayList<Favorites>()
//        notesCursor?.apply {
//            while (moveToNext()) {
//                val id = getInt(getColumnIndexOrThrow(DBContract.FavoriteColum._ID))
//                val category = getString(getColumnIndexOrThrow(DBContract.FavoriteColum.CATEGORY))
//                favList.add(Favorites(id, category))
//            }
//        }
//        return favList
//    }
}