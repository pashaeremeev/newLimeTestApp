package com.example.new_practice.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.new_practice.domain.models.ChannelModel
import com.example.new_practice.domain.models.EpgModel
import com.example.new_practice.domain.usecase.GetChannelsInfoUseCase
import com.example.new_practice.domain.usecase.GetEpgByChannelIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class VideoViewModel(
    private val getChannelsInfoUseCase: GetChannelsInfoUseCase,
    private val getEpgByChannelIdUseCase: GetEpgByChannelIdUseCase
): ViewModel() {

    fun getCatalogOfChannel(): LiveData<List<ChannelModel>> {
        return getChannelsInfoUseCase.launch().asLiveData(Dispatchers.IO)
    }

    fun getEpgForChannel(channelId: Flow<Int?>): LiveData<EpgModel> {
        return getEpgByChannelIdUseCase.launch(channelId).asLiveData(Dispatchers.IO)
    }
}