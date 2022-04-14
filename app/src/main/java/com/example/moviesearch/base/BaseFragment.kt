package com.example.moviesearch.base

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.moviesearch.api.RequestApi
import com.example.moviesearch.db.dao.MovieDAO
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import retrofit2.Call

abstract class BaseFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    val requestApi: RequestApi by instance()
    val movieDAO: MovieDAO by instance()
    private var baseActivity: BaseActivity? = null

    fun showSnackBar(message: String) = baseActivity?.showSnackBar(message)

    fun showProgressDialog() = baseActivity?.showProgressDialog()

    fun hideProgressDialog() = baseActivity?.hideProgressDialog()

    fun TextInputLayout.isFilled(editText: TextInputEditText): Boolean {
        if (editText.text?.trim().isNullOrEmpty()) {
            error = "Please, fill out this field."
            return false
        }
        error = null
        return true
    }

    fun loadImage(imageView: ImageView, string: String?,
                  onCompleted: (byteArray: ByteArray) -> Unit) =
        baseActivity?.loadImage(imageView, string, onCompleted)

    fun View.hideKeyboard() = baseActivity?.hideKeyboard(this)

    fun <T> Call<T>.callBack(success: (response: T) -> Unit, failure: () -> Unit = {}) =
        baseActivity?.callBack(this, success, failure)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            baseActivity = context
        }
    }

    override fun onDetach() {
        baseActivity = null
        super.onDetach()
    }

    abstract fun initialize()
}