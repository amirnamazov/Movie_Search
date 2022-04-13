package com.example.moviesearch.api.request

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RequestClient {

    private val baseUrl = "http://www.omdbapi.com/"

    private val okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .connectTimeout(15, TimeUnit.SECONDS)
        .addInterceptor { chain ->

            val httpUrl = chain.request().url().newBuilder()
                .addQueryParameter("apikey", "55d6c4fd")
                .build()

            val request = chain.request().newBuilder()
                .url(httpUrl)
                .build()

            chain.proceed(request)
        }
        .build()

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    val instance: RequestApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        retrofit.create(RequestApi::class.java)
    }
}