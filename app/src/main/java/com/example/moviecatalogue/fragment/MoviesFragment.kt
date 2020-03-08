package com.example.moviecatalogue.fragment


import android.app.ActionBar
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviecatalogue.adapter.ListMoviesAdapter
import com.example.moviecatalogue.model.MainViewModel
import com.example.moviecatalogue.model.Moviews
import com.example.moviecatalogue.R
import com.example.moviecatalogue.database.FavHelper
import com.example.moviecatalogue.mapping.MappingHelper
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.android.synthetic.main.fragment_movies.progressbar
import kotlinx.android.synthetic.main.fragment_search_movie.*

/**
 * A simple [Fragment] subclass.
 */
class MoviesFragment : Fragment() {


    private lateinit var moviesAdapter: ListMoviesAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var favHelper: FavHelper

    private val moviess = ArrayList<Moviews>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_movies, container, false)
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
        rv_heroes.setHasFixedSize(true)
        rv_heroes.layoutManager = LinearLayoutManager(requireContext())
        rv_heroes.adapter = moviesAdapter

        mainViewModel.getMovies().observe(this, Observer {  movie->
            if(movie!=null){

                val favMovieCur = favHelper.movieQueryAll()
                val favMovierlist = MappingHelper.movieMapCursorToArrayList(favMovieCur)
                val favMovieShow = ArrayList<Moviews>()

                Log.d("tagMovie","$favMovierlist")
                movie.forEach {
                    val movieData = it
                    favMovierlist.forEach {
                        if(movieData.id == it.id){
                            movieData.isFav = true
                            Log.d("tag", "$movieData")
                        }
                    }
                    favMovieShow.add(movieData)
                }

                moviesAdapter.setData(favMovieShow)
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

                moviesAdapter.filter.filter(query)

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                moviesAdapter.filter.filter(newText)
                return false
            }
        })

        searchView.setOnCloseListener {
            showLoading(true)
            true
        }

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
