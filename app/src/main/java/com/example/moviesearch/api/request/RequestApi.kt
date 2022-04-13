package com.example.moviesearch.api.request

import com.example.moviesearch.api.model.response.*
import retrofit2.Call
import retrofit2.http.*

interface RequestApi {

    @GET("/")
    fun getData(@Query("i") i: String = "tt3896198",): Call<MovieDetails>
}