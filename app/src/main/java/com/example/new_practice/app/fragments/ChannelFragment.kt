package com.example.new_practice.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.new_practice.app.ClickChannelListener
import com.example.new_practice.R
import com.example.new_practice.app.AppViewModelFactory
import com.example.new_practice.app.ChannelViewModel
import com.example.new_practice.app.adapters.ChannelAdapter
import com.example.new_practice.data.repos.ChannelRepositoryImpl
import com.example.new_practice.domain.models.ChannelModel

@UnstableApi
class ChannelFragment(private val position: Int): Fragment() {

    private lateinit var channelRepo: ChannelRepositoryImpl
    private lateinit var vm: ChannelViewModel
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

        vm = ViewModelProvider(
            requireActivity(),
            AppViewModelFactory(requireContext())
        )[ChannelViewModel::class.java]

        channelRepo = ChannelRepositoryImpl.getInstance(context)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val searchTextView = view.findViewById<TextView>(R.id.searchTextView)
        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(view.context)

        vm.getEpgs().observe(viewLifecycleOwner) {
            if (adapter == null) {
                adapter = ChannelAdapter(
                    view.context,
                    it,
                    object : ClickChannelListener {
                        override fun invoke(channel: ChannelModel?) {
                            clickOnChannelView(channel!!)
                        }
                    },
                    object : ClickChannelListener {
                        override fun invoke(channel: ChannelModel?) {
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

        vm.getChannels(channelRepo.searchFlow).observe(viewLifecycleOwner) {
            val result: DiffUtil.DiffResult? = if (position == 1) {
                val newFavChannels: MutableList<ChannelModel> = mutableListOf()
                for (i in it.indices) {
                    val channel: ChannelModel = it[i]
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

        return view
    }

    private fun clickOnChannelView(channel: ChannelModel) {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.videoContainer, VideoFragment.getInstance(channel.id))
        fragmentTransaction.commitAllowingStateLoss()
    }

    private fun clickOnFavoriteView(channel: ChannelModel) {
        vm.changeFavChannel(channelId = channel.id)
    }
}