package com.example.moviecatalogue.fragment


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.moviecatalogue.R
import com.example.moviecatalogue.activity.DetailMoviesActivity
import com.example.moviecatalogue.activity.MainActivity
import com.example.moviecatalogue.adapter.ListMoviesAdapter
import com.example.moviecatalogue.model.MainViewModel
import com.example.moviecatalogue.model.Moviews
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.android.synthetic.main.fragment_search_movie.*
import kotlinx.android.synthetic.main.fragment_search_movie.progressbar


class SearchMovieFragment : Fragment() {

    companion object{
        var EXTRA_QUERY = "extra_query"
    }

    private lateinit var mainViewModel: MainViewModel
    private lateinit var movieAdapter: ListMoviesAdapter
    private val moviess = ArrayList<Moviews>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_movie, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieAdapter = ListMoviesAdapter(moviess)
        movieAdapter.notifyDataSetChanged()

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        searchMovies.setHasFixedSize(true)
        searchMovies.layoutManager = LinearLayoutManager(activity?.applicationContext)
        searchMovies.adapter = movieAdapter


        searchMovie()

        movieAdapter.setOnItemClickCallback(object : ListMoviesAdapter.OnItemClickCallback{
            override fun goToDetailMovie(data: Moviews) {
                val intent= Intent(activity,  DetailMoviesActivity::class.java)
                intent.putExtra(DetailMoviesActivity.EXTRA_FILM, data)
                startActivity(intent)
            }
        })
    }

    fun searchMovie(){
        showLoading(true)
        mainViewModel.searchMovie(getString(R.string.language), EXTRA_QUERY)

        mainViewModel.getMovies().observe(this, Observer { movieItems ->
            if(movieItems!=null){
                movieAdapter.setData(movieItems)
                showLoading(false)

            }
        })
    }

    private fun showLoading(state: Boolean){
        if(state){
            progressbar.visibility= View.VISIBLE
        }
        else{
            progressbar.visibility= View.GONE
        }
    }


}
