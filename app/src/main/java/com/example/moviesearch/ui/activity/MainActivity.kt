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
    }

    private fun bindViewPagerWithTabs() {
        setupViewPager()
        TabLayoutMediator(b.tabLayout, b.viewPager2) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    private fun setupViewPager() {
        val fragList = listOf(SearchFragment(), SearchFragment(false))
        val adapter = ViewPagerAdapter(this, fragList)

        b.viewPager2.adapter = adapter
    }
}