package com.example.moviecatalogue.activity

import android.content.ContentValues
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.moviecatalogue.R
import com.example.moviecatalogue.database.DBContract
import com.example.moviecatalogue.database.DBContract.FavoriteColum.Companion.CONTENT_TV_URI
import com.example.moviecatalogue.database.FavHelper
import com.example.moviecatalogue.model.TvShow
import kotlinx.android.synthetic.main.activity_detail_tv_show.*

class DetailTvShowActivity : AppCompatActivity() {

    private var mMenuItem : Menu? = null
    private lateinit var favHelper: FavHelper
    private lateinit var shows: TvShow
    private var isFav = false
    private lateinit var uri: Uri

    companion object{
        const val EXTRA_SHOW = "extra_show"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tv_show)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        favHelper = FavHelper.getInstance(applicationContext)
        favHelper.open()

        shows = intent.getParcelableExtra(EXTRA_SHOW) as TvShow
        isFav = shows.isFav
        Log.d("tag", "$shows")
        uri = Uri.parse(DBContract.FavoriteColum.CONTENT_TV_URI.toString()+"/"+shows.id)

        tv_title.text = shows.name
        tv_air_date.text = shows.first_air_date
        tv_desc_show.text = shows.overview

        Glide.with(baseContext)
            .load("https://image.tmdb.org/t/p/w185${shows.poster_path}")
            .apply(RequestOptions().override(200, 250))
            .into(img_show)
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
                Toast.makeText(this@DetailTvShowActivity, R.string.text_deleted, Toast.LENGTH_SHORT).show()

            }else{
                mMenuItem!!.getItem(0).setIcon(resources.getDrawable(R.drawable.ic_favorite_black_24dp))
                isFav = true
                val values = ContentValues()
                values.put(DBContract.FavoriteColum.TV_ID, shows.id)
                values.put(DBContract.FavoriteColum.TV_NAME, shows.name)
                values.put(DBContract.FavoriteColum.TV_FIRST_AIR_DATE, shows.first_air_date)
                values.put(DBContract.FavoriteColum.TV_OVERVIEW, shows.overview)
                values.put(DBContract.FavoriteColum.TV_POSTER_PATH, shows.poster_path)

                contentResolver.insert(CONTENT_TV_URI, values)

            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        favHelper.close()
    }
}
