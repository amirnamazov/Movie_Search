package com.example.moviesearch.base

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.moviesearch.R
import com.example.moviesearch.api.RequestApi
import com.example.moviesearch.db.dao.MovieDAO
import com.example.moviesearch.utils.Connection.isNetworkConnected
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

abstract class BaseActivity : AppCompatActivity(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    val requestApi: RequestApi by instance()
    val movieDAO: MovieDAO by instance()
    private lateinit var progressDialog: AlertDialog

    private fun checkConnection(execute: () -> Unit) {
        if (isNetworkConnected()) execute()
        else showSnackBar("No connection!")
    }

    fun showSnackBar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).apply {
            view.layoutParams = (view.layoutParams as FrameLayout.LayoutParams).apply {
                gravity = Gravity.TOP
            }
        }.show()
    }

    fun showProgressDialog(message: String = "Processing...") {
        val l = LinearLayoutCompat.inflate(this, R.layout.dialog_progress, null)
        progressDialog = AlertDialog.Builder(this).setCancelable(false).setView(l).create()
        l.findViewById<TextView>(R.id.txt_loading)!!.text = message
        progressDialog.show()
    }

    fun hideProgressDialog() = progressDialog.dismiss()

    private fun showResponseDialog(message: String, title: String = "Response") {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setNeutralButton("Close") { dg, _, ->
                dg.dismiss()
            }
            .show()
    }

    fun hideKeyboard(v: View) {
        val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    fun TextInputLayout.isFilled(editText: TextInputEditText): Boolean {
        if (editText.text?.trim().isNullOrEmpty()) {
            error = "Please, fill out this field."
            return false
        }
        error = null
        return true
    }

    fun loadImage(imageView: ImageView, string: String?,
                  onCompleted: (byteArray: ByteArray?) -> Unit, byteArray: ByteArray? = null) =
        Glide.with(imageView.context)
            .load(string ?: byteArray)
            .apply(
                RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .priority(Priority.IMMEDIATE))
            .fitCenter()
            .override(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    onCompleted(null)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    val bitmap = (resource as BitmapDrawable).bitmap
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    val bitmapData: ByteArray = stream.toByteArray()
                    onCompleted(bitmapData)
                    return false
                }
            })
            .into(imageView)

    fun <T> callBack(call: Call<T>, success: (response: T) -> Unit,
                     failure: () -> Unit = {}) =
        checkConnection {
            showProgressDialog()
            call.enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    hideProgressDialog()
                    if (response.isSuccessful && response.body() != null) {
                        success(response.body()!!)
                    } else {
                        showResponseDialog(response.message(), "Error")
                        failure()
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    hideProgressDialog()
                    showResponseDialog(t.message!!, "Error")
                    failure()
                }
            })
        }
}