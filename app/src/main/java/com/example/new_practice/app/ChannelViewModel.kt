package com.example.new_practice.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.new_practice.domain.models.ChannelModel
import com.example.new_practice.domain.models.EpgModel
import com.example.new_practice.domain.usecase.ChangeFavoriteChannelUseCase
import com.example.new_practice.domain.usecase.GetChannelsInfoUseCase
import com.example.new_practice.domain.usecase.GetEpgsUseCase
import com.example.new_practice.domain.usecase.SearchChannelsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ChannelViewModel(
    private val changeFavoriteChannelUseCase: ChangeFavoriteChannelUseCase,
    private val getChannelsInfoUseCase: GetChannelsInfoUseCase,
    private val getEpgsUseCase: GetEpgsUseCase,
    private val searchChannelsUseCase: SearchChannelsUseCase
): ViewModel() {

    fun getChannels(searchParam: Flow<String>): LiveData<List<ChannelModel>> {
        return getChannelsInfoUseCase.launch()
            .combine(searchChannelsUseCase.launch(searchParam)) {
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
            }.asLiveData(context = Dispatchers.IO)
    }

    fun getEpgs(): LiveData<List<EpgModel>> {
        return getEpgsUseCase.launch().asLiveData(context = Dispatchers.IO)
    }

    fun changeFavChannel(channelId: Int) {
        changeFavoriteChannelUseCase.launch(channelId = channelId)
    }
}