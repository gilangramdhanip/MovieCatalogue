package com.example.moviecatalogue.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.moviecatalogue.database.DBContract.FavoriteColum.Companion.MOVIE_ID
import com.example.moviecatalogue.database.DBContract.FavoriteColum.Companion.MOVIE_OVERVIEW
import com.example.moviecatalogue.database.DBContract.FavoriteColum.Companion.MOVIE_POPULARITY
import com.example.moviecatalogue.database.DBContract.FavoriteColum.Companion.MOVIE_POSTER_PATH
import com.example.moviecatalogue.database.DBContract.FavoriteColum.Companion.MOVIE_RELEASE_DATE
import com.example.moviecatalogue.database.DBContract.FavoriteColum.Companion.MOVIE_TABLE
import com.example.moviecatalogue.database.DBContract.FavoriteColum.Companion.MOVIE_TITLE
import com.example.moviecatalogue.database.DBContract.FavoriteColum.Companion.MOVIE_VOTE_AVERAGE
import com.example.moviecatalogue.database.DBContract.FavoriteColum.Companion.MOVIE_VOTE_COUNT
import com.example.moviecatalogue.database.DBContract.FavoriteColum.Companion.TV_FIRST_AIR_DATE
import com.example.moviecatalogue.database.DBContract.FavoriteColum.Companion.TV_ID
import com.example.moviecatalogue.database.DBContract.FavoriteColum.Companion.TV_NAME
import com.example.moviecatalogue.database.DBContract.FavoriteColum.Companion.TV_OVERVIEW
import com.example.moviecatalogue.database.DBContract.FavoriteColum.Companion.TV_POPULARITY
import com.example.moviecatalogue.database.DBContract.FavoriteColum.Companion.TV_POSTER_PATH
import com.example.moviecatalogue.database.DBContract.FavoriteColum.Companion.TV_SHOW_TABLE
import com.example.moviecatalogue.database.DBContract.FavoriteColum.Companion.TV_VOTE_AVERAGE
import com.example.moviecatalogue.database.DBContract.FavoriteColum.Companion.TV_VOTE_COUNT

internal class DBHelper(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object{
        private const val DB_NAME = "moviecalatagoueDB"
        private const val DB_VERSION = 1

        private  val SQL_CREATE_TABLE_MOVIE_FAV = "CREATE TABLE $MOVIE_TABLE" +
                "($MOVIE_ID INTEGER PRIMARY KEY, " +
                "$MOVIE_POPULARITY REAL, " +
                "$MOVIE_VOTE_COUNT INTEGER, " +
                "$MOVIE_POSTER_PATH TEXT, " +
                "$MOVIE_TITLE TEXT, " +
                "$MOVIE_VOTE_AVERAGE REAL, " +
                "$MOVIE_OVERVIEW TEXT, " +
                "$MOVIE_RELEASE_DATE TEXT)"

        private  val SQL_CREATE_TABLE_TV_FAV = "CREATE TABLE $TV_SHOW_TABLE" +
                "($TV_ID INTEGER PRIMARY KEY, " +
                "$TV_NAME TEXT, " +
                "$TV_POPULARITY REAL, " +
                "$TV_VOTE_COUNT INTEGER, " +
                "$TV_FIRST_AIR_DATE TEXT, " +
                "$TV_VOTE_AVERAGE REAL, " +
                "$TV_OVERVIEW TEXT, " +
                "$TV_POSTER_PATH TEXT)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        Log.d("berhasil", "$SQL_CREATE_TABLE_MOVIE_FAV")
        Log.d("berhasil", "$SQL_CREATE_TABLE_TV_FAV")
        db?.execSQL(SQL_CREATE_TABLE_MOVIE_FAV)
        db?.execSQL(SQL_CREATE_TABLE_TV_FAV)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $MOVIE_TABLE")
        db?.execSQL("DROP TABLE IF EXISTS $TV_SHOW_TABLE")
        onCreate(db)
    }
}