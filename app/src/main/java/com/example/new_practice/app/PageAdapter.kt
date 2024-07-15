package com.example.new_practice.app

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.media3.common.util.UnstableApi
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.new_practice.app.presentation.fragments.ChannelFragment

class PageAdapter(fragmentManager: FragmentManager?, lifecycle: Lifecycle?) :
    FragmentStateAdapter(fragmentManager!!, lifecycle!!) {

    private val fragments: ArrayList<ChannelFragment> = arrayListOf()

    @UnstableApi
    override fun createFragment(position: Int): Fragment {
        if (fragments.getOrNull(position) == null) {
            fragments.add(position, ChannelFragment.newInstance(isFav = position == 1))
        }
        return fragments[position]
    }

    @UnstableApi
    fun setSearchQuery(text: String) {
        fragments.forEach {
            it.onQueryChanged(text)
        }
    }

    override fun getItemCount(): Int {
        return NUM_PAGE
    }

    companion object {
        private const val NUM_PAGE = 2
    }
}