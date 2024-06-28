package com.example.new_practice.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.new_practice.Channel
import com.example.new_practice.ClickChannelListener
import com.example.new_practice.DownloadChannels
import com.example.new_practice.Epg
import com.example.new_practice.R
import com.example.new_practice.adapters.ChannelAdapter
import com.example.new_practice.repos.ChannelRepo
import com.example.new_practice.repos.EpgRepo

@UnstableApi
class FavChannelFragment : Fragment() {
    private var channelRepo: ChannelRepo? = null
    private var epgRepo: EpgRepo? = null
    private var adapter: ChannelAdapter? = null
    private var liveData: LiveData<ArrayList<Channel>>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_fav_channel_list, container, false)
        channelRepo = ChannelRepo(context)
        epgRepo = EpgRepo.getInstance(context)
        liveData = ChannelRepo.liveData
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerFavView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        adapter = ChannelAdapter(
            view.context,
            epgRepo!!.epgs,
            object : ClickChannelListener {
                override fun invoke(channel: Channel?) {
                    clickOnChannelView(channel!!)
                }
            }
        )
        liveData!!.observe(
            viewLifecycleOwner
        ) { list: ArrayList<Channel> ->
            val favChannels: ArrayList<Channel> = ArrayList<Channel>()
            val favEpg: ArrayList<Epg> = ArrayList<Epg>()
            for (i in list.indices) {
                val channel: Channel = list[i]
                val epg: Epg = epgRepo!!.epgs.get(i)
                if (channel.isFavorite) {
                    favChannels.add(channel)
                    favEpg.add(epg)
                }
            }
            adapter!!.setChannels(favChannels)
            adapter!!.setEpgs(favEpg)
            adapter!!.notifyDataSetChanged()
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

    private fun clickOnFavoriteView(channel: Channel, recyclerView: RecyclerView): Void? {
        val channelList: ArrayList<Channel> = liveData!!.value!!
        for (i in channelList.indices) {
            if (channelList[i].id === channel.id) {
                channelList[i].isFavorite = !channelList[i].isFavorite
                channelRepo!!.saveChannels(channelList)
            }
        }
        return null
    }
}