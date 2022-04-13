package com.example.moviesearch.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.moviesearch.base.BaseActivity
import com.example.moviesearch.databinding.ActivityMainBinding
import com.example.moviesearch.ui.adapter.ViewPagerAdapter
import com.example.moviesearch.ui.fragment.SearchFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : BaseActivity() {

    private lateinit var b: ActivityMainBinding
    private val tabTitles = listOf("Browse", "Recent",)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        bindViewPagerWithTabs()

//        requestApi.getData().callBack({
//
//        })
    }

    private fun bindViewPagerWithTabs() {
        setupViewPager()
        TabLayoutMediator(b.tabLayout, b.viewPager2) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    private fun setupViewPager() {
        val fragList = List<Fragment>(2) { SearchFragment() }
        val adapter = ViewPagerAdapter(this, fragList)

        b.viewPager2.adapter = adapter
//        b.viewPager2.isUserInputEnabled = false
//        b.viewPager2.offscreenPageLimit = 1
//        b.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
//            override fun onPageSelected(position: Int) {
//
//            }
//        })
    }
}