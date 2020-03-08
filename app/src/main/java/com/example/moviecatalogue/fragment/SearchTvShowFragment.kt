package com.example.moviecatalogue.fragment


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.moviecatalogue.R
import com.example.moviecatalogue.activity.DetailMoviesActivity
import com.example.moviecatalogue.adapter.ListMoviesAdapter
import com.example.moviecatalogue.adapter.ListShowAdapter
import com.example.moviecatalogue.model.MainViewModel
import com.example.moviecatalogue.model.Moviews
import com.example.moviecatalogue.model.TvShow
import kotlinx.android.synthetic.main.fragment_search_movie.*
import kotlinx.android.synthetic.main.fragment_search_movie.progressbar
import kotlinx.android.synthetic.main.fragment_search_tv_show.*

/**
 * A simple [Fragment] subclass.
 */
class SearchTvShowFragment : Fragment() {

    companion object{
        var EXTRA_QUERY = "extra_query"
    }

    private lateinit var mainViewModel: MainViewModel
    private lateinit var showAdapter: ListShowAdapter
    private val tvshow = ArrayList<TvShow>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_tv_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showAdapter = ListShowAdapter(tvshow)
        showAdapter.notifyDataSetChanged()

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)

        searchTv.setHasFixedSize(true)
        searchTv.layoutManager = LinearLayoutManager(activity?.applicationContext)
        searchTv.adapter = showAdapter


        searchTvShow()

        showAdapter.setOnItemClickCallback(object : ListShowAdapter.OnItemClickCallback{
            override fun goToDetailShow(data: TvShow) {
                val intent= Intent(activity,  DetailMoviesActivity::class.java)
                intent.putExtra(DetailMoviesActivity.EXTRA_FILM, data)
                startActivity(intent)
            }
        })
    }

    fun searchTvShow(){
        showLoading(true)
        mainViewModel.searchTvShows(getString(R.string.language), EXTRA_QUERY)

        mainViewModel.getTVShows().observe(this, Observer { tvshow ->
            if(tvshow!=null){
                showAdapter.setData(tvshow)
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
