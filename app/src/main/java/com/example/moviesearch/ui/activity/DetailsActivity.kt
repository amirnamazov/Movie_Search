package com.example.moviesearch.ui.activity

import android.os.Bundle
import com.example.moviesearch.base.BaseActivity
import com.example.moviesearch.databinding.ActivityDetailsBinding
import com.example.moviesearch.databinding.ItemDetailsBinding
import com.example.moviesearch.db.model.Movie

class DetailsActivity : BaseActivity() {

    private lateinit var b: ActivityDetailsBinding
    private lateinit var bDetails: ItemDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(b.root)

        val movie = intent.getParcelableExtra<Movie>("Movie")
        loadImage(b.poster, null, {}, movie!!.moviePoster)

        b.etUpdate.setText(movie.myRating)

        b.btnUpdate.setOnClickListener {
            if (b.etUpdateLayout.isFilled(b.etUpdate)) {
                if (b.etUpdate.text.toString().toDouble() <= 10) {
                    val movie = Movie(movie.id, movie.movieDetails, movie.moviePoster, b.etUpdate.text.toString())
                    movieDAO.update(movie)
                    showSnackBar("Rating updated")
                    hideKeyboard(it)
                } else {
                    showSnackBar("Not more than 10")
                }
            }
        }

        addItem("Title: ", movie.movieDetails.Title)
        addItem("Year: ", movie.movieDetails.Year)
        addItem("Rated: ", movie.movieDetails.Rated)
        addItem("Released: ", movie.movieDetails.Released)
        addItem("Run time: ", movie.movieDetails.Runtime)
        addItem("Genre: ", movie.movieDetails.Genre)
        addItem("Director: ", movie.movieDetails.Director)
        addItem("Writer: ", movie.movieDetails.Writer)
        addItem("Actors: ", movie.movieDetails.Actors)
        addItem("Plot: ", movie.movieDetails.Plot)
        addItem("Language: ", movie.movieDetails.Language)
        addItem("Country: ", movie.movieDetails.Country)
        addItem("Awards: ", movie.movieDetails.Awards)
        addItem("Metascore: ", movie.movieDetails.Metascore)
        addItem("IMDB rating: ", movie.movieDetails.imdbRating)
        addItem("IMDP votes: ", movie.movieDetails.imdbVotes)
        addItem("Type: ", movie.movieDetails.Type)
        addItem("DVD: ", movie.movieDetails.DVD)
        addItem("Box office: ", movie.movieDetails.BoxOffice)
        addItem("Production: ", movie.movieDetails.Production)
        addItem("Website: ", movie.movieDetails.Website)
    }

    private fun addItem(title: String, desc: String) {
        bDetails = ItemDetailsBinding.inflate(layoutInflater)
        bDetails.tvTitle.text = title
        bDetails.tvDesc.text = desc
        b.linear.addView(bDetails.root)
    }
}