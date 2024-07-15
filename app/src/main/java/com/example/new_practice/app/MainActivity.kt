package com.example.new_practice.app

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.media3.common.util.UnstableApi
import androidx.viewpager2.widget.ViewPager2
import com.example.new_practice.R
import com.example.new_practice.app.viewModels.ChannelViewModel
import com.example.new_practice.data.repos.ChannelRepositoryImpl
import com.example.new_practice.data.repos.EpgRepositoryImpl
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val tabNames = arrayOf("Все", "Избранные")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.pager)

        val adapter = PageAdapter(supportFragmentManager, lifecycle)
        viewPager?.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab: TabLayout.Tab, position: Int ->
            tab.text = tabNames[position]
        }.attach()

        val searchView = findViewById<SearchView>(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            @UnstableApi
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.setSearchQuery(query ?: "")
                return false
            }

            @UnstableApi
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.setSearchQuery(newText ?: "")
                return false
            }
        })

    }
}