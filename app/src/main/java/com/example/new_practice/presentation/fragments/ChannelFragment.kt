package com.example.new_practice.presentation.fragments

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
import com.example.new_practice.presentation.ClickChannelListener
import com.example.new_practice.R
import com.example.new_practice.presentation.adapters.ChannelAdapter
import com.example.new_practice.data.repos.ChannelRepositoryImpl
import com.example.new_practice.data.repos.EpgRepositoryImpl
import com.example.new_practice.domain.models.ChannelModel
import com.example.new_practice.domain.usecase.ChangeFavoriteChannelUseCase
import com.example.new_practice.domain.usecase.GetChannelsInfoUseCase
import com.example.new_practice.domain.usecase.SearchChannelsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine

@UnstableApi
class ChannelFragment(private val position: Int): Fragment() {

    private lateinit var channelRepo: ChannelRepositoryImpl
    private lateinit var epgRepo: EpgRepositoryImpl
    private lateinit var getChannelsInfoUseCase: GetChannelsInfoUseCase
    private lateinit var searchChannelsUseCase: SearchChannelsUseCase
    private lateinit var changeFavoriteChannelUseCase: ChangeFavoriteChannelUseCase
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

        epgRepo = EpgRepositoryImpl.getInstance(context)
        channelRepo = ChannelRepositoryImpl.getInstance(context)

        getChannelsInfoUseCase = GetChannelsInfoUseCase(channelRepository = channelRepo)
        searchChannelsUseCase = SearchChannelsUseCase(channelRepository = channelRepo)
        changeFavoriteChannelUseCase = ChangeFavoriteChannelUseCase(channelRepository = channelRepo)


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
        // Получаем каналы из БД и текст из поля поиска
        getChannelsInfoUseCase.launch()
            .combine(searchChannelsUseCase.launch(channelRepo.searchFlow)) {
                channels, searchChannels ->
            var filteredChannelsList: ArrayList<ChannelModel> = arrayListOf()
            if (searchChannels.isNotEmpty()) {
                for (channel in searchChannels) {
                    val channelFromAll = channels.firstOrNull {it.id == channel.id}
                    if (channelFromAll != null) {
                        filteredChannelsList.add(channel)
                    }
                }
            } else {
                filteredChannelsList = channels as ArrayList<ChannelModel>
            }
            return@combine filteredChannelsList
        }.asLiveData(context = Dispatchers.IO).observe(viewLifecycleOwner) {
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
        fragmentTransaction.replace(R.id.videoCntainer, VideoFragment.getInstance(channel.id))
        fragmentTransaction.commitAllowingStateLoss()
    }

    private fun clickOnFavoriteView(channel: ChannelModel) {
        //channelRepo.changeFav(channelId = channel.id)
        changeFavoriteChannelUseCase.launch(channelId = channel.id)
    }
}