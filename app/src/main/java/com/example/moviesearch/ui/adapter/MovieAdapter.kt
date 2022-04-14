package com.example.moviesearch.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearch.base.BaseFragment
import com.example.moviesearch.databinding.ItemMovieBinding
import com.example.moviesearch.db.model.Movie

class MovieAdapter(private val frag: Fragment, private val list: List<Movie>,
                   private val browse: Boolean)
    : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private lateinit var b: ItemMovieBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        b = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return MovieViewHolder(b.root)
    }

    override fun onBindViewHolder(h: MovieViewHolder, p: Int) = h.bind(p)

    override fun getItemViewType(p: Int): Int = p

    override fun getItemCount(): Int = list.size

    interface ItemClick {
        fun onItemClick(id: Int)
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: Int) {
            b.tvYear.text = "Loading..."
            if (!browse) setTexts(p)
            else try {
                (frag as BaseFragment).loadImage(b.poster, list[p].movieDetails.Poster) {
                    setTexts(p)
                    frag.movieDAO.insert(Movie(0, list[p].movieDetails, it!!, list[p].myRating))
                }
            } catch (e: Exception) {
                setTexts(p)
                (frag as BaseFragment).movieDAO.insert(Movie(0, list[p].movieDetails,
                    null, list[p].myRating))
            }

            b.root.setOnClickListener {
                (frag as ItemClick).onItemClick(list[p].id)
            }
        }

        private fun setTexts(p: Int) {
            b.tvTitle.text = list[p].movieDetails.Title
            b.tvYear.text = list[p].movieDetails.Year
            b.tvGenre.text = list[p].movieDetails.Genre
        }
    }
}