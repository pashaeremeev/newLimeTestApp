package com.example.new_practice.app.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.new_practice.domain.models.ChannelModel
import com.example.new_practice.domain.models.EpgModel
import com.example.new_practice.domain.usecase.ChangeFavoriteChannelUseCase
import com.example.new_practice.domain.usecase.GetChannelsInfoUseCase
import com.example.new_practice.domain.usecase.GetEpgsUseCase
import com.example.new_practice.domain.usecase.SearchChannelsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class ChannelViewModel @Inject constructor(
    private val changeFavoriteChannelUseCase: ChangeFavoriteChannelUseCase,
    private val getChannelsInfoUseCase: GetChannelsInfoUseCase,
    private val getEpgsUseCase: GetEpgsUseCase,
    private val searchChannelsUseCase: SearchChannelsUseCase
): ViewModel() {

    val searchFlow: MutableStateFlow<String> = MutableStateFlow("")

    fun getChannels(): LiveData<List<ChannelModel>> {
        return getChannelsInfoUseCase.launch()
            .combine(searchChannelsUseCase.launch(searchFlow)) {
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