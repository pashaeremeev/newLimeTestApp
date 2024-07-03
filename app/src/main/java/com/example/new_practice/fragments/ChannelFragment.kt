package com.example.new_practice.fragments

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine

@UnstableApi
class ChannelFragment(private val position: Int): Fragment() {

    private lateinit var channelRepo: ChannelRepo
    private lateinit var epgRepo: EpgRepo
    private var adapter: ChannelAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(
            R.layout.all_channels,
            container,
            false)

        epgRepo = EpgRepo.getInstance(context)
        channelRepo = ChannelRepo.getInstance(context)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val searchTextView = view.findViewById<TextView>(R.id.searchTextView)
        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(view.context)
        epgRepo.epgs.observe(viewLifecycleOwner) {
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
                result?.dispatchUpdatesTo(adapter!!)
            }

        }
        // Получаем каналы из БД и текст из поля поиска
        channelRepo.channelsFlow.combine(channelRepo.searchFlow) { channels, filter ->
            var filteredChannelsList: ArrayList<Channel> = arrayListOf()
            if (filter.isNotEmpty()) {
                for (channel in channels) {
                    if (channel.name.contains(filter, ignoreCase = true)) {
                        filteredChannelsList.add(channel)
                    }
                }
            } else {
                filteredChannelsList = channels as ArrayList<Channel>
            }
            return@combine filteredChannelsList
        }.asLiveData(context = Dispatchers.IO).observe(viewLifecycleOwner) {
            val result: DiffUtil.DiffResult? = if (position == 1) {
                val newFavChannels: MutableList<Channel> = mutableListOf()
                for (i in it.indices) {
                    val channel: Channel = it[i]
                    if (channel.isFavorite) {
                        newFavChannels.add(channel)
                    }
                }
                adapter?.setChannels(newFavChannels)
            } else {
                adapter?.setChannels(it)
            }
            if (adapter?.getChannels()!!.isEmpty()) {
                searchTextView.visibility = View.VISIBLE
            } else {
                searchTextView.visibility = View.GONE
            }
            result?.dispatchUpdatesTo(adapter!!)
        }

        DownloadChannels.downloadChannels(channelRepo, epgRepo) { isSuccess ->
            if (!isSuccess!!) {
                return@downloadChannels null
            }
            null
        }

        return view
    }

    private fun clickOnChannelView(channel: Channel) {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.videoCntainer, VideoFragment.getInstance(channel.id))
        fragmentTransaction.commitAllowingStateLoss()
    }

    private fun clickOnFavoriteView(channel: Channel) {
        channelRepo.changeFav(channelId = channel.id)
    }
}