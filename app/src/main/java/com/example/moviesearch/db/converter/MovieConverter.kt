package com.example.moviesearch.db.converter

import androidx.room.TypeConverter
import com.example.moviesearch.model.MovieDetails
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object MovieConverter {
    @TypeConverter
    fun toMovieDetails(value: String): MovieDetails {
        val gson = Gson()
        val type = object : TypeToken<MovieDetails>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromMovieDetails(value: MovieDetails): String {
        val gson = Gson()
        return gson.toJson(value)
    }
}