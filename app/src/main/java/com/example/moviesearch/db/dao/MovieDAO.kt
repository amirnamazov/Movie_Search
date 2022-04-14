package com.example.moviesearch.db.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.moviesearch.db.model.Movie

@Dao
interface MovieDAO {

    @Insert(onConflict = REPLACE)
    fun insert(movie: Movie): Long

    @Update
    fun update(movie: Movie)

    @Delete
    fun delete(movie: Movie)

    @Query("DELETE FROM table_movies")
    fun deleteAll(): Int

    @Query("SELECT * FROM table_movies")
    fun getAllMovies(): List<Movie>
}