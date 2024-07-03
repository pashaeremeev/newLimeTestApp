package com.example.new_practice.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.new_practice.storage.entities.Channel
import com.example.new_practice.ClickChannelListener
import com.example.new_practice.DownloadChannels
import com.example.new_practice.R
import com.example.new_practice.adapters.ChannelAdapter
import com.example.new_practice.repos.ChannelRepo
import com.example.new_practice.repos.EpgRepo

@UnstableApi
class ChannelFragment(private val position: Int): Fragment() {

    private lateinit var channelRepo: ChannelRepo
    private lateinit var epgRepo: EpgRepo
    private var adapter: ChannelAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(
            R.layout.fragment_channel_list,
            container,
            false)

        epgRepo = EpgRepo.getInstance(context)
        channelRepo = ChannelRepo.getInstance(context)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        val root = inflater.inflate(
            R.layout.activity_main,
            container,
            false)

        val searchView = root.findViewById<SearchView>(R.id.searchView)

        recyclerView.layoutManager = LinearLayoutManager(view.context)
        epgRepo?.epgs?.observe(viewLifecycleOwner) {
            if (adapter == null) {
                adapter = ChannelAdapter(
                    view.context,
                    it,
                    object : ClickChannelListener {
                        override fun invoke(channel: Channel?) {
                            clickOnChannelView(channel!!)
                        }
                    },
                    object : ClickChannelListener {
                        override fun invoke(channel: Channel?) {
                            clickOnFavoriteView(channel!!)
                        }
                    }
                )
                recyclerView.adapter = adapter
            } else {
                val result = adapter?.setEpgs(it)
                //adapter?.notifyDataSetChanged()
                result?.dispatchUpdatesTo(adapter!!)
            }

        }
        channelRepo.channels.observe(viewLifecycleOwner) {
            val result: DiffUtil.DiffResult?
            if (position == 1) {
                val newFavChannels: MutableList<Channel> = mutableListOf()
                for (i in it.indices) {
                    val channel: Channel = it[i]
                    if (channel.isFavorite) {
                        newFavChannels.add(channel)
                    }
                }
                result = adapter?.setChannels(newFavChannels)
            } else {
                result = adapter?.setChannels(it)
            }
            result?.dispatchUpdatesTo(adapter!!)
            //adapter?.notifyDataSetChanged()
        }
        DownloadChannels.downloadChannels(channelRepo!!, epgRepo!!) { isSuccess ->
            if (!isSuccess!!) {
                return@downloadChannels null
            }
            null
        }
        recyclerView.adapter = adapter
        return view
    }

    private fun clickOnChannelView(channel: Channel): Void? {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.videoCntainer, VideoFragment.getInstance(channel.id))
        fragmentTransaction.commitAllowingStateLoss()
        return null
    }

    private fun clickOnFavoriteView(channel: Channel) {
        channelRepo?.changeFav(channelId = channel.id)
    }
}