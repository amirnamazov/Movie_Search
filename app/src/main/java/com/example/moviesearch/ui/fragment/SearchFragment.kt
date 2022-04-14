package com.example.moviesearch.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesearch.base.BaseFragment
import com.example.moviesearch.databinding.FragmentSearchBinding
import com.example.moviesearch.db.model.Movie
import com.example.moviesearch.ui.activity.DetailsActivity
import com.example.moviesearch.ui.adapter.MovieAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment(private val browse: Boolean = true) : BaseFragment(), MovieAdapter.ItemClick {

    private lateinit var b: FragmentSearchBinding

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, saved: Bundle?): View {
        b = FragmentSearchBinding.inflate(i, c, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialize()
    }

    override fun initialize() {

        val plotList = listOf("Select", "Short", "Full")
        b.spPlot.adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, plotList)

        val yearList = MutableList(100) { (2022 - it).toString() }
        yearList.add(0, "Select")
        b.spYears.adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, yearList)

        if (!browse) getDataFromLocalDB()

        b.btnSearch.setOnClickListener {
            if (browse) {
                if (b.etSearchLayout.isFilled(b.etSearch)) {
                    it.hideKeyboard()
                    getDataFromServer()
                }
            } else {
                it.hideKeyboard()
                getDataFromLocalDB()
            }

        }
    }

    private fun getDataFromServer() = requestApi.getMovieData(
        b.etSearch.text.toString(),
        if (b.spYears.selectedItem.toString() == "Select") null else b.spYears.selectedItem.toString(),
        if (b.spPlot.selectedItem.toString() == "Select") null else b.spPlot.selectedItem.toString(),
    )
        .callBack({ movie ->
            when {
                movie.Response.toBoolean() -> {
                    setAdapter(listOf(Movie(0, movie, null, "0")))
                }
                movie.Error!!.isNotEmpty() -> {
                    showSnackBar(movie.Error)
                }
                else -> {
                    showSnackBar("Something went wrong.")
                }
            }
        })

    private fun getDataFromLocalDB() {
        showProgressDialog()
        CoroutineScope(Dispatchers.IO).launch {
            withContext(this.coroutineContext) {
                requireActivity().runOnUiThread {
                    val list = movieDAO.getAllMovies().filterIndexed { _, movie ->
                        movie.movieDetails.Title.contains(b.etSearch.text.toString())
                    }
                    setAdapter(list)
                    hideProgressDialog()
                }
            }
        }
    }

    private fun setAdapter(list: List<Movie>) {
        b.rvResults.layoutManager = LinearLayoutManager(requireContext())
        b.rvResults.adapter = MovieAdapter(this, list, browse)
    }

    override fun onItemClick(id: Int) {
        Intent(requireContext(), DetailsActivity :: class.java).apply {
            val movie = movieDAO.getAllMovies().find { it.id == id }
            putExtra("Movie", movie)
            startActivity(this)
        }
    }
}