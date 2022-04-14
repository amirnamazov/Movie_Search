package com.example.moviesearch.db.instance

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moviesearch.db.converter.MovieConverter
import com.example.moviesearch.db.dao.MovieDAO
import com.example.moviesearch.db.model.Movie

@Database(entities = [Movie :: class], version = 1, exportSchema = false)
@TypeConverters(MovieConverter :: class)
abstract class MovieDB : RoomDatabase() {
    abstract val movieDAO: MovieDAO

    companion object {
        @Volatile
        private var INSTANCE : MovieDB? = null
        fun getInstance(context: Context) : MovieDB {
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MovieDB :: class.java,
                        "movies_data_db"
                    )
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return instance
            }
        }
    }
}

