package com.example.moviesearch.api

import com.example.moviesearch.db.model.Movie
import com.example.moviesearch.model.MovieDetails
import retrofit2.Call
import retrofit2.http.*

interface RequestApi {

    @GET("/")
    fun getMovieData(@Query("t") title: String,
                     @Query("y") year: String? = null,
                     @Query("plot") plot: String? = null,): Call<MovieDetails>
}