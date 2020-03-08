package com.example.moviecatalogue

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.example.moviecatalogue.database.FavHelper
import com.example.moviecatalogue.mapping.MappingHelper
import com.example.moviecatalogue.model.Moviews
import java.util.ArrayList

internal class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<Moviews>()
    private lateinit var favHelper: FavHelper

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        //Ini berfungsi untuk melakukan refresh saat terjadi perubahan.
        favHelper = FavHelper.getInstance(mContext)
        favHelper.open()

        val movieFavCur = favHelper.movieQueryAll()
        val movieFav = MappingHelper.movieMapCursorToArrayList(movieFavCur)

        mWidgetItems.addAll(movieFav)
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        val bitmap = Glide.with(mContext.applicationContext)
            .asBitmap()
            .load("https://image.tmdb.org/t/p/w185${mWidgetItems[position].poster_path}")
            .submit(512, 512)
            .get()

        rv.setImageViewBitmap(R.id.imageView, bitmap)

        val extras = bundleOf(
            FavoriteMovieWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.tv_title, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}