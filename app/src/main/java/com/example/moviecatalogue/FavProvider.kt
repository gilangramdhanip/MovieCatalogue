package com.example.moviecatalogue

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.moviecatalogue.database.DBContract.AUTHORITY
import com.example.moviecatalogue.database.DBContract.FavoriteColum.Companion.CONTENT_MOVIE_URI
import com.example.moviecatalogue.database.DBContract.FavoriteColum.Companion.CONTENT_TV_URI
import com.example.moviecatalogue.database.DBContract.FavoriteColum.Companion.MOVIE_TABLE
import com.example.moviecatalogue.database.DBContract.FavoriteColum.Companion.TV_SHOW_TABLE
import com.example.moviecatalogue.database.FavHelper

class FavProvider : ContentProvider() {

    companion object{
        private const val MOVIE_FAVORITE = 100
        private const val MOVIE_FAVORITE_ID = 101
        private const val TV_FAVORITE = 200
        private const val TV_FAVORITE_ID = 201
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favHelper: FavHelper

        init{
            sUriMatcher.addURI(AUTHORITY, MOVIE_TABLE, MOVIE_FAVORITE)
            sUriMatcher.addURI(AUTHORITY, "$MOVIE_TABLE/#", MOVIE_FAVORITE_ID)
            sUriMatcher.addURI(AUTHORITY, TV_SHOW_TABLE, TV_FAVORITE)
            sUriMatcher.addURI(AUTHORITY, "$TV_SHOW_TABLE/#", TV_FAVORITE_ID)
        }
    }

    override fun onCreate(): Boolean {
        favHelper = FavHelper.getInstance(context as Context)
        favHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val cursor: Cursor?
        when(sUriMatcher.match(uri)){
            MOVIE_FAVORITE -> cursor = favHelper.movieQueryAll()
            TV_FAVORITE -> cursor = favHelper.tvQueryAll()
            else -> cursor = null
        }

        return  cursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when(sUriMatcher.match(uri)){
            MOVIE_FAVORITE -> favHelper.movieAddFav(values)
            TV_FAVORITE -> favHelper.tvAddFav(values)
            else -> 0
        }

        val contentUri: Uri = when(sUriMatcher.match(uri)){
            MOVIE_FAVORITE -> CONTENT_MOVIE_URI
            else -> CONTENT_TV_URI
        }

        context?.contentResolver?.notifyChange(contentUri, null)

        return Uri.parse("$contentUri/$added")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {

        val deleted: Int = when(sUriMatcher.match(uri)){
            MOVIE_FAVORITE_ID -> favHelper.movieDeleteFav(uri.lastPathSegment!!.toInt())
            TV_FAVORITE_ID -> favHelper.tvDeleteFav(uri.lastPathSegment!!.toInt())
            else -> 0
        }

        val contentUri: Uri = when(sUriMatcher.match(uri)){
            MOVIE_FAVORITE_ID -> CONTENT_MOVIE_URI
            else -> CONTENT_TV_URI
        }

        context?.contentResolver?.notifyChange(contentUri, null)

        return deleted
    }
}
