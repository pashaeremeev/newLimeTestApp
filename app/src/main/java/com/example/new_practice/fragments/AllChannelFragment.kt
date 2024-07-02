package com.example.new_practice.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.new_practice.storage.entities.Channel
import com.example.new_practice.ClickChannelListener
import com.example.new_practice.DownloadChannels
import com.example.new_practice.R
import com.example.new_practice.adapters.ChannelAdapter
import com.example.new_practice.diffUtils.ChannelDiffUtilCallback
import com.example.new_practice.diffUtils.EpgDiffUtilCallback
import com.example.new_practice.repos.ChannelRepo
import com.example.new_practice.repos.EpgRepo

@UnstableApi
class AllChannelFragment : Fragment() {

    private lateinit var channelRepo: ChannelRepo
    private var epgRepo: EpgRepo? = null
    private var adapter: ChannelAdapter? = null
    //private var channelLiveData: LiveData<ArrayList<Channel>>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(
            R.layout.fragment_all_channel_list,
            container,
            false)
        epgRepo = EpgRepo.getInstance(context)
        channelRepo = ChannelRepo.getInstance(context)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
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
        channelRepo?.channels?.observe(viewLifecycleOwner) {
            val result = adapter?.setChannels(it)
            result?.dispatchUpdatesTo(adapter!!)
            //adapter?.notifyDataSetChanged()
        }
        DownloadChannels.downloadChannels(channelRepo!!, epgRepo!!) { isSuccess ->
            if (!isSuccess!!) {
                return@downloadChannels null
            }
//            epgRepo?.epgs?.observe(viewLifecycleOwner) {
//                val result = adapter?.setEpgs(it)
//                //adapter?.notifyDataSetChanged()
//                result?.dispatchUpdatesTo(adapter!!)
//            }
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