package com.example.new_practice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.viewpager2.widget.ViewPager2
import com.example.new_practice.repos.ChannelRepo
import com.example.new_practice.repos.EpgRepo
import com.example.new_practice.storage.entities.Channel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private val tabNames = arrayOf("Все", "Избранные")
    private lateinit var channelRepo: ChannelRepo
    private lateinit var epgRepo: EpgRepo
    private val filteredList = mutableListOf<Channel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.pager)

        channelRepo = ChannelRepo.getInstance(baseContext)
        epgRepo = EpgRepo.getInstance(baseContext)

        var adapter = PageAdapter(supportFragmentManager, lifecycle)
        viewPager?.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab: TabLayout.Tab, position: Int ->
            tab.text = tabNames[position]
        }.attach()

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                channelRepo.channels.observe(this@MainActivity) {
                    if ((newText?.isEmpty()) == false) {
//                        filteredList.clear()
//                        for (channel in it) {
//                            if (channel.name.lowercase().contains(newText.toString(), ignoreCase = false)) {
//                                filteredList.add(channel)
//                            }
//                        }
                    }
                }
                return false
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}