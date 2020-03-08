package com.example.favoriteapps.activity

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.favoriteapps.R
import com.example.favoriteapps.database.DBContract
import com.example.favoriteapps.database.DBContract.FavoriteColum.Companion.CONTENT_MOVIE_URI
import com.example.favoriteapps.model.Moviews
import kotlinx.android.synthetic.main.activity_movies_detail.*

class DetailMoviesActivity : AppCompatActivity() {

    private var mMenuItem : Menu? = null
    private lateinit var moviews: Moviews
    private var isFav = false

    private lateinit var uri: Uri

    companion object {
        const val EXTRA_FILM = "extra_film"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies_detail)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        moviews = intent.getParcelableExtra(EXTRA_FILM) as Moviews
        isFav = moviews.isFav
        Log.d("tag", "$moviews")

        uri = Uri.parse(DBContract.FavoriteColum.CONTENT_MOVIE_URI.toString()+"/"+moviews.id)

        textView.text = moviews.title
        textView3.text = moviews.overview
        textView2.text = moviews.popularity.toString()

        Glide.with(baseContext)
            .load("https://image.tmdb.org/t/p/w185${moviews.poster_path}")
            .apply(RequestOptions().override(200, 250))
            .into(imageView)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        mMenuItem = menu
        menuInflater.inflate(R.menu.menu_favorite, menu)
        if(isFav){
            mMenuItem!!.getItem(0).setIcon(resources.getDrawable(R.drawable.ic_favorite_black_24dp))
        }

        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId == R.id.fa_act){
            if(isFav){
                mMenuItem!!.getItem(0).setIcon(resources.getDrawable(R.drawable.ic_favorite_border_black_24dp))
                isFav = false

                contentResolver.delete(uri, null, null)
                Toast.makeText(this@DetailMoviesActivity, R.string.text_deleted, Toast.LENGTH_SHORT).show()
            }else{
                mMenuItem!!.getItem(0).setIcon(resources.getDrawable(R.drawable.ic_favorite_black_24dp))
                isFav = true
                val values = ContentValues()
                values.put(DBContract.FavoriteColum.MOVIE_ID, moviews.id)
                values.put(DBContract.FavoriteColum.MOVIE_TITLE, moviews.title)
                values.put(DBContract.FavoriteColum.MOVIE_POPULARITY, moviews.popularity)
                values.put(DBContract.FavoriteColum.MOVIE_OVERVIEW, moviews.overview)
                values.put(DBContract.FavoriteColum.MOVIE_POSTER_PATH, moviews.poster_path)

                contentResolver.insert(CONTENT_MOVIE_URI, values)

            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
