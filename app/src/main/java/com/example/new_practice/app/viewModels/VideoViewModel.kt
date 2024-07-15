package com.example.new_practice.app.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.new_practice.domain.models.ChannelModel
import com.example.new_practice.domain.models.EpgModel
import com.example.new_practice.domain.usecase.GetChannelsInfoUseCase
import com.example.new_practice.domain.usecase.GetEpgByChannelIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val getChannelsInfoUseCase: GetChannelsInfoUseCase,
    private val getEpgByChannelIdUseCase: GetEpgByChannelIdUseCase
): ViewModel() {

    val currentChannelId: MutableStateFlow<Int?> = MutableStateFlow(null)

    fun getCatalogOfChannel(): LiveData<List<ChannelModel>> {
        return getChannelsInfoUseCase.launch().asLiveData(Dispatchers.IO)
    }

    fun getEpgForChannel(/*channelId: Flow<Int?>*/): LiveData<EpgModel> {
        return getEpgByChannelIdUseCase.launch(currentChannelId).asLiveData(Dispatchers.IO)
    }
}