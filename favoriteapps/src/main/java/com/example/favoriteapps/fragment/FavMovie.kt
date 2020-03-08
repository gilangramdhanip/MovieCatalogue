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
import com.example.favoriteapps.adapter.ListMoviesAdapter
import com.example.favoriteapps.database.DBContract.FavoriteColum.Companion.CONTENT_MOVIE_URI
import com.example.favoriteapps.mapping.MappingHelper
import com.example.favoriteapps.model.MainViewModel
import com.example.favoriteapps.model.Moviews

import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_fav_movie.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class FavMovie : Fragment() {

    private lateinit var moviesAdapter: ListMoviesAdapter
    private lateinit var mainViewModel: MainViewModel

    private val moviess = ArrayList<Moviews>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_fav_movie, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moviesAdapter = ListMoviesAdapter(moviess)
        moviesAdapter.notifyDataSetChanged()

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)

        showLoading(true)

        mainViewModel.setMovies(getString(R.string.language))
        fav_movie.setHasFixedSize(true)
        fav_movie.layoutManager = LinearLayoutManager(requireContext())
        fav_movie.adapter = moviesAdapter

        loadFavAsync()


        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)



        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                Log.d("FragmentFav", "$CONTENT_MOVIE_URI")
                loadFavAsync()
            }
        }

        activity?.applicationContext?.contentResolver?.registerContentObserver(CONTENT_MOVIE_URI, true, myObserver)


    }

    @SuppressLint("Recycle")
    private fun loadFavAsync() {
        GlobalScope.launch(Dispatchers.Main){
            showLoading(true)
            val defferedFav = async(Dispatchers.IO){
                val cursor = activity?.applicationContext?.contentResolver?.query(CONTENT_MOVIE_URI, null, null, null, null) as? Cursor
                MappingHelper.movieMapCursorToArrayList(cursor)
            }
            val movies = defferedFav.await()
            showLoading(false)
            if (movies.size >0 ){
                moviesAdapter.setData(movies)
            }else{
                showSnackBarMessage("Data tidak ditemukan")
            }

        }
    }

    private fun showSnackBarMessage(s: String) {
        Snackbar.make(fav_movie, s, Snackbar.LENGTH_SHORT).show()
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
