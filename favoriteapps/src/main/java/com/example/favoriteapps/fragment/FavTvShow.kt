package com.example.favoriteapps.fragment


import android.annotation.SuppressLint
import android.database.ContentObserver
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteapps.R
import com.example.favoriteapps.adapter.ListShowAdapter
import com.example.favoriteapps.database.DBContract
import com.example.favoriteapps.mapping.MappingHelper
import com.example.favoriteapps.model.MainViewModel
import com.example.favoriteapps.model.TvShow

import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_fav_tv_show.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class FavTvShow : Fragment() {

    private lateinit var showAdapter: ListShowAdapter
    private lateinit var mainViewModel: MainViewModel

    private val shows = ArrayList<TvShow>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_fav_tv_show, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showAdapter = ListShowAdapter(shows)
        showAdapter.notifyDataSetChanged()

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)

        showLoading(true)

        mainViewModel.setShows(getString(R.string.language))
        favShow.setHasFixedSize(true)
        favShow.layoutManager = LinearLayoutManager(activity?.applicationContext)
        favShow.adapter = showAdapter

        LoadFavoriteAsync()


        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)



        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                Log.d("FragmentFav", "${DBContract.FavoriteColum.CONTENT_TV_URI}")
                LoadFavoriteAsync()
            }
        }

        activity?.applicationContext?.contentResolver?.registerContentObserver(DBContract.FavoriteColum.CONTENT_TV_URI, true, myObserver)


    }

    @SuppressLint("Recycle")
    private fun LoadFavoriteAsync() {
        GlobalScope.launch(Dispatchers.Main){
            showLoading(true)
            val defferedFav = async(Dispatchers.IO){
                val cursor = activity?.applicationContext?.contentResolver?.query(DBContract.FavoriteColum.CONTENT_TV_URI, null, null, null, null) as? Cursor
                MappingHelper.tvMapCursorToArrayList(cursor)
            }
            val tvshow = defferedFav.await()
            showLoading(false)
            if (tvshow.size >0 ){
                showAdapter.setData(tvshow)
            }else{
                showSnackBarMessage("Data tidak ditemukan")
            }

        }
    }

    private fun showSnackBarMessage(s: String) {
        Snackbar.make(favShow, s, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressbar.visibility = View.VISIBLE
        } else {
            progressbar.visibility = View.GONE
        }
    }

}
