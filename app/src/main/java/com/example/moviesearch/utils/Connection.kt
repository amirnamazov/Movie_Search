package com.example.moviesearch.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object Connection {

    fun Context.isNetworkConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val activeNetwork = cm.activeNetwork
        if (activeNetwork != null) {
            val nc = cm.getNetworkCapabilities(activeNetwork)
            return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
        return false
    }
}