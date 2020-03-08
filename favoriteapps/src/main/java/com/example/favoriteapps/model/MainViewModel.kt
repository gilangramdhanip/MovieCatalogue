package com.example.favoriteapps.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.favoriteapps.api.DataRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : AndroidViewModel(Application()) {

    companion object{
        const val KEY_API = "8908a1d59ebed18e66c34b99f01d57fc"
    }

    private val listMovie =  MutableLiveData<ArrayList<Moviews>>()
    private val listTV =  MutableLiveData<ArrayList<TvShow>>()
    private val apiService = DataRepository.create()

    fun setMovies(lang: String){
        val dataMovie = ArrayList<Moviews>()
        apiService.getMovies(KEY_API, lang).enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.d("FailureLog", t.message)
            }

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                val data = response.body()
                Log.d("ResponseLog", response.toString())
                dataMovie.addAll(data!!.results)
                listMovie.postValue(dataMovie)
            }
        })
    }

    fun setShows(lang: String){
        val dataTV = ArrayList<TvShow>()
        apiService.getTVShow(KEY_API,lang).enqueue(object: Callback<TvShowResponse>{
            override fun onFailure(call: Call<TvShowResponse>, t: Throwable) {
                Log.d("FailureLog", t.message)
            }

            override fun onResponse(call: Call<TvShowResponse>, response: Response<TvShowResponse>) {
                val data = response.body()
                Log.d("ResponeLog", data!!.toString())
                dataTV.addAll(data.results)
                listTV.postValue(dataTV)
            }
        })
    }

    fun getMovies(): LiveData<ArrayList<Moviews>> {
        return listMovie
    }

    fun getTVShows(): LiveData<ArrayList<TvShow>> {
        return listTV
    }

    fun searchMovie(lang: String, query: String){
        val dataMovie = ArrayList<Moviews>()
        apiService.searchMovies(KEY_API, lang, query).enqueue(object: Callback<MovieResponse>{
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.d("FailureLog", t.message)
            }

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                val data = response.body()
                Log.d("ResponseLog", response.toString())
                dataMovie.addAll(data!!.results)
                listMovie.postValue(dataMovie)
            }
        })
    }

    fun searchTvShows(lang: String, string: String){
        val shows = ArrayList<TvShow>()
        apiService.searchTVShows(KEY_API,lang, string).enqueue(object : Callback<TvShowResponse>{
            override fun onFailure(call: Call<TvShowResponse>, t: Throwable) {
                Log.d("FailureLog", t.message)
            }

            override fun onResponse(call: Call<TvShowResponse>, response: Response<TvShowResponse>) {
                val data = response.body()
                Log.d("ResponseLog", response.toString())
                shows.addAll(data!!.results)
                listTV.postValue(shows)
            }
        })
    }



}