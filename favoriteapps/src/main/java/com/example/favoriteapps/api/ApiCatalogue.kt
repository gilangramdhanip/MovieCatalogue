package com.example.favoriteapps.api


import com.example.favoriteapps.model.MovieResponse
import com.example.favoriteapps.model.TvShowResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiCatalogue {

    @GET("3/discover/movie")
    fun getMovies(@Query("api_key") key: String,
                  @Query("language") string: String): retrofit2.Call<MovieResponse>

    @GET("3/discover/tv")
    fun getTVShow(@Query("api_key") key: String,
                  @Query("language") string: String): Call<TvShowResponse>

    @GET("3/search/movie")
    fun searchMovies(@Query("api_key") key: String,
                     @Query("language") lang: String,
                     @Query("query") movieName: String): Call<MovieResponse>

    @GET("3/search/tv")
    fun searchTVShows(@Query("api_key") key: String,
                      @Query("language") lang: String,
                      @Query("query") tvName: String): Call<TvShowResponse>

    @GET("3/discover/movie")
    fun getTodayRelease(@Query("api_key") key: String,
                        @Query("primary_release_date.gte") releaseDateGte: String,
                        @Query("primary_release_date.gte") releaseDateLte: String,
                        @Query("language") lang: String): Call<MovieResponse>
}