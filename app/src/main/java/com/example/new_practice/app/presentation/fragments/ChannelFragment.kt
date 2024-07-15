package com.example.new_practice.app.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.new_practice.app.ClickChannelListener
import com.example.new_practice.R
import com.example.new_practice.app.viewModels.ChannelViewModel
import com.example.new_practice.app.adapters.ChannelAdapter
import com.example.new_practice.domain.models.ChannelModel
import dagger.hilt.android.AndroidEntryPoint

@UnstableApi
@AndroidEntryPoint
class ChannelFragment: Fragment() {

    private val vm: ChannelViewModel by viewModels()
    private var adapter: ChannelAdapter? = null
    private var isFavourite: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(
            R.layout.all_channels,
            container,
            false)

        isFavourite = requireArguments().getBoolean(IS_FAV)

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

        vm.getChannels().observe(viewLifecycleOwner) {
            val result: DiffUtil.DiffResult? = if (isFavourite) {
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

    fun onQueryChanged(text: String) {
        vm.searchFlow.value = text
    }

    private fun clickOnChannelView(channel: ChannelModel) {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.videoContainer, VideoFragment.newInstance(channel.id))
        fragmentTransaction.commitAllowingStateLoss()
    }

    private fun clickOnFavoriteView(channel: ChannelModel) {
        vm.changeFavChannel(channelId = channel.id)
    }

    companion object {
        private const val IS_FAV = "IS_FAV"
        fun newInstance(isFav: Boolean): ChannelFragment {
            val fragment = ChannelFragment()
            val bundle = Bundle()
            bundle.putBoolean(IS_FAV, isFav)
            fragment.arguments = bundle
            return fragment
        }
    }
}