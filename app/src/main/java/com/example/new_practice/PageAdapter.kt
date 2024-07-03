package com.example.new_practice

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.media3.common.util.UnstableApi
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.new_practice.fragments.ChannelFragment

class PageAdapter(fragmentManager: FragmentManager?, lifecycle: Lifecycle?) :
    FragmentStateAdapter(fragmentManager!!, lifecycle!!) {

    @UnstableApi
    override fun createFragment(position: Int): Fragment {
        return ChannelFragment(position)
    }

    override fun getItemCount(): Int {
        return NUM_PAGE
    }

    companion object {
        private const val NUM_PAGE = 2
    }
}