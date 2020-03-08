package com.example.moviecatalogue.fragment


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.moviecatalogue.R
import com.example.moviecatalogue.adapter.ListMoviesAdapter
import com.example.moviecatalogue.database.FavHelper
import com.example.moviecatalogue.mapping.MappingHelper
import com.example.moviecatalogue.model.MainViewModel
import com.example.moviecatalogue.model.Moviews
import kotlinx.android.synthetic.main.fragment_fav_movie.*
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.android.synthetic.main.fragment_movies.progressbar

/**
 * A simple [Fragment] subclass.
 */
class FavMovie : Fragment() {

    private lateinit var moviesAdapter: ListMoviesAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var favHelper: FavHelper

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

        favHelper = FavHelper.getInstance(context!!)
        favHelper.open()

        moviesAdapter = ListMoviesAdapter(moviess)
        moviesAdapter.notifyDataSetChanged()

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)

        showLoading(true)

        mainViewModel.setMovies(getString(R.string.language))
        fav_movie.setHasFixedSize(true)
        fav_movie.layoutManager = LinearLayoutManager(requireContext())
        fav_movie.adapter = moviesAdapter

        mainViewModel.getMovies().observe(this, Observer {  movie->
            if(movie!=null){

                val FavMovieCur = favHelper.movieQueryAll()
                val FavMovierlist = MappingHelper.movieMapCursorToArrayList(FavMovieCur)
                val FavMovieShow = ArrayList<Moviews>()

                Log.d("tagMovieFav","$FavMovierlist")
                movie.forEach {
                    val movieData = it
                    FavMovierlist.forEach {
                        if(movieData.id == it.id){
                            movieData.isFav = true
                            Log.d("tag", "$movieData")
                            FavMovieShow.add(movieData)
                        }
                    }
                }

                moviesAdapter.setData(FavMovieShow)
                showLoading(false)
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        favHelper.close()
    }


    private fun showLoading(state: Boolean) {
        if (state) {
            progressbar.visibility = View.VISIBLE
        } else {
            progressbar.visibility = View.GONE
        }
    }

}
