package com.example.moviesearch

import android.os.Bundle
import com.example.moviesearch.base.BaseActivity
import com.example.moviesearch.databinding.ActivityMainBinding
import okhttp3.ResponseBody

class MainActivity : BaseActivity() {

    private lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        requestApi.getData().callBack({
            b.tvTitle.text = it.string()
        })
    }
}