package com.example.moviesearch.base

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.moviesearch.R
import com.example.moviesearch.api.RequestApi
import com.example.moviesearch.utils.Connection.isNetworkConnected
import com.google.android.material.snackbar.Snackbar
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class BaseActivity : AppCompatActivity(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    val requestApi: RequestApi by instance()
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

    private fun showProgressDialog(message: String = "Processing...") {
        val l = LinearLayoutCompat.inflate(this, R.layout.dialog_progress, null)
        progressDialog = AlertDialog.Builder(this).setCancelable(false).setView(l).create()
        l.findViewById<TextView>(R.id.txt_loading)!!.text = message
        progressDialog.show()
    }

    private fun hideProgressDialog() = progressDialog.dismiss()

    private fun showResponseDialog(message: String, title: String = "Response") {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setNeutralButton("Close") { dg, _, ->
                dg.dismiss()
            }
            .show()
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun <T> Call<T>.callBack(success: (response: T) -> Unit,
                     failure: () -> Unit = {}) =
        checkConnection {
            showProgressDialog()
            enqueue(object : Callback<T> {
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