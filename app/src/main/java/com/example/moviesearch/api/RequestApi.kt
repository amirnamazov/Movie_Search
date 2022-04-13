package com.example.moviesearch.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface RequestApi {

    @GET("/")
    fun getData(@Query("i") i: String = "tt3896198",): Call<ResponseBody>
}