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
import com.example.moviecatalogue.adapter.ListShowAdapter
import com.example.moviecatalogue.database.FavHelper
import com.example.moviecatalogue.mapping.MappingHelper
import com.example.moviecatalogue.model.MainViewModel
import com.example.moviecatalogue.model.Moviews
import com.example.moviecatalogue.model.TvShow
import kotlinx.android.synthetic.main.fragment_fav_tv_show.*
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.android.synthetic.main.fragment_movies.progressbar
import kotlinx.android.synthetic.main.fragment_tv_show.*

/**
 * A simple [Fragment] subclass.
 */
class FavTvShow : Fragment() {

    private lateinit var showAdapter: ListShowAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var favHelper: FavHelper

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

        favHelper = FavHelper.getInstance(context!!)
        favHelper.open()

        showAdapter = ListShowAdapter(shows)
        showAdapter.notifyDataSetChanged()

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)

        showLoading(true)

        mainViewModel.setShows(getString(R.string.language))
        fav_show.setHasFixedSize(true)
        fav_show.layoutManager = LinearLayoutManager(requireContext())
        fav_show.adapter = showAdapter

        mainViewModel.getTVShows().observe(this, Observer {  favtvshow->
            if(favtvshow!=null){

                val favTvCur = favHelper.tvQueryAll()
                val favTvList = MappingHelper.tvMapCursorToArrayList(favTvCur)
                val favTvShow = ArrayList<TvShow>()

                Log.d("tagTvShowFav","$favTvList")
                favtvshow.forEach {
                    val tvData = it
                    favTvList.forEach {
                        if(tvData.id == it.id){
                            tvData.isFav = true
                            Log.d("tag", "$tvData")
                            favTvShow.add(tvData)
                        }
                    }

                }

                showAdapter.setData(favTvShow)
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
