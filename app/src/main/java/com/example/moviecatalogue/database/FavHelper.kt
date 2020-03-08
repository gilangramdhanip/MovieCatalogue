package com.example.moviecatalogue.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.moviecatalogue.database.DBContract.FavoriteColum.Companion.MOVIE_ID
import com.example.moviecatalogue.database.DBContract.FavoriteColum.Companion.MOVIE_TABLE
import com.example.moviecatalogue.database.DBContract.FavoriteColum.Companion.TV_ID
import com.example.moviecatalogue.database.DBContract.FavoriteColum.Companion.TV_SHOW_TABLE
import java.sql.SQLException

class FavHelper(context: Context){

    private var databaseHelper: DBHelper = DBHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object{
        private const val MOVIE_FAV_TABLE = MOVIE_TABLE
        private const val TV_FAV_TABLE = TV_SHOW_TABLE

        private lateinit var databaseHelper: DBHelper
        private var INSTANCE: FavHelper? = null

        fun getInstance(context: Context): FavHelper =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: FavHelper(context)
            }

    }


@Throws(SQLException::class)
fun open(){
    database = databaseHelper.writableDatabase
}

fun close(){
    databaseHelper.close()

    if(database.isOpen){
        database.close()
    }
}

fun movieQueryAll(): Cursor{
    return database.query(
        MOVIE_FAV_TABLE,
        null,
        null,
        null,
        null,
        null,
        "$MOVIE_ID ASC"
    )
}

fun movieAddFav(values: ContentValues?): Long{
    return database.insert(MOVIE_FAV_TABLE, null, values)
}

fun movieDeleteFav(id: Int): Int{
    return database.delete(MOVIE_FAV_TABLE, "$MOVIE_ID = '$id'", null)
}

fun tvQueryAll(): Cursor{
    return database.query(
        TV_FAV_TABLE,
        null,
        null,
        null,
        null,
        null,
        "$TV_ID ASC"
    )
}

fun tvAddFav(values: ContentValues?): Long{
    return database.insert(TV_FAV_TABLE, null, values)
}

fun tvDeleteFav(id: Int): Int{
    return database.delete(TV_FAV_TABLE, "$TV_ID = '$id'", null)
}

}