package com.acacia.randomchat.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainViewPagerAdapter(
    fragmentActivty: FragmentActivity,
): FragmentStateAdapter(fragmentActivty) {

    override fun getItemCount(): Int = 1

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> HomeFragment.newInstance()
            else -> error("No Fragment")
        }
    }
}