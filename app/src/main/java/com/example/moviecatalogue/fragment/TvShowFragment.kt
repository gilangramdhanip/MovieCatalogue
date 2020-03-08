package com.example.moviecatalogue.fragment


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviecatalogue.adapter.ListShowAdapter
import com.example.moviecatalogue.model.MainViewModel
import com.example.moviecatalogue.model.TvShow
import com.example.moviecatalogue.R
import com.example.moviecatalogue.database.FavHelper
import com.example.moviecatalogue.mapping.MappingHelper
import com.example.moviecatalogue.model.Moviews
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.android.synthetic.main.fragment_tv_show.*
import kotlinx.android.synthetic.main.fragment_tv_show.progressbar

/**
 * A simple [Fragment] subclass.
 */
class TvShowFragment : Fragment() {

    private lateinit var showAdapter: ListShowAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var favHelper: FavHelper

    private val shows = ArrayList<TvShow>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_tv_show, container, false)
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

        rv_heroes2.setHasFixedSize(true)
        rv_heroes2.layoutManager = LinearLayoutManager(requireContext())
        rv_heroes2.adapter = showAdapter

        mainViewModel.getTVShows().observe(this, Observer {  tvshow->
            if(tvshow!=null){

                val tvVarCur = favHelper.tvQueryAll()
                val tvFavList = MappingHelper.tvMapCursorToArrayList(tvVarCur)
                val tvFavShow = ArrayList<TvShow>()

                Log.d("tagMovie","$tvFavList")
                tvshow.forEach {
                    val tvData = it
                    tvFavList.forEach {
                        if(tvData.id == it.id){
                            tvData.isFav = true
                            Log.d("tag", "$tvData")
                        }
                    }
                    tvFavShow.add(tvData)
                }

                showAdapter.setData(tvFavShow)
                showLoading(false)
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
//                SearchTvShowFragment.EXTRA_QUERY = query!!
//                fragmentManager?.beginTransaction()
//                    ?.replace(R.id.tvshow_rl, SearchTvShowFragment())?.commit()
//
//                rv_heroes2.visibility = View.GONE

                showAdapter.filter.filter(query)

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                showAdapter.filter.filter(newText)
                return false
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressbar.visibility = View.VISIBLE
        } else {
            progressbar.visibility = View.GONE
        }
    }
}
