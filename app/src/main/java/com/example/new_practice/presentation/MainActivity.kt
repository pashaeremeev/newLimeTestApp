package com.example.new_practice.presentation

import android.app.PictureInPictureParams
import android.os.Build
import android.os.Bundle
import android.util.Rational
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.viewpager2.widget.ViewPager2
import com.example.new_practice.R
import com.example.new_practice.data.repos.ChannelRepositoryImpl
import com.example.new_practice.data.repos.EpgRepositoryImpl
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {

    private val tabNames = arrayOf("Все", "Избранные")
    private lateinit var channelRepo: ChannelRepositoryImpl
    private lateinit var epgRepo: EpgRepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.pager)

        channelRepo = ChannelRepositoryImpl.getInstance(baseContext)
        epgRepo = EpgRepositoryImpl.getInstance(baseContext)

        val adapter = PageAdapter(supportFragmentManager, lifecycle)
        viewPager?.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab: TabLayout.Tab, position: Int ->
            tab.text = tabNames[position]
        }.attach()

        val searchView = findViewById<SearchView>(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                channelRepo.setSearchFilter(query ?: "")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                channelRepo.setSearchFilter(newText ?: "")
                return false
            }
        })

    }

//    @RequiresApi(Build.VERSION_CODES.R)
//    override fun onUserLeaveHint() {
//        super.onUserLeaveHint()
//        val ratio = Rational(16, 9)
//        val pipBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            PictureInPictureParams.Builder()
//        } else {
//            TODO("VERSION.SDK_INT < O")
//        }
//        pipBuilder.setAspectRatio(ratio).build()
//        enterPictureInPictureMode(pipBuilder.build())
//    }
}