package com.example.moviesearch.db.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.ParcelField
import com.example.moviesearch.model.MovieDetails
import kotlinx.parcelize.Parcelize

@Entity(tableName = "table_movies")
@Parcelize
data class Movie (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val movieDetails: MovieDetails,
    val moviePoster: ByteArray? = null,
    val myRating: String
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Movie

        if (id != other.id) return false
        if (movieDetails != other.movieDetails) return false
        if (moviePoster != null) {
            if (other.moviePoster == null) return false
            if (!moviePoster.contentEquals(other.moviePoster)) return false
        } else if (other.moviePoster != null) return false
        if (myRating != other.myRating) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + movieDetails.hashCode()
        result = 31 * result + (moviePoster?.contentHashCode() ?: 0)
        result = 31 * result + myRating.hashCode()
        return result
    }
}