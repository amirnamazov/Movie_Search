package com.example.moviesearch.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(activity: FragmentActivity, private val list: List<Fragment>) :
    FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }
}