package com.example.new_practice.app

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.new_practice.data.repos.ChannelRepositoryImpl
import com.example.new_practice.data.repos.EpgRepositoryImpl
import com.example.new_practice.domain.usecase.ChangeFavoriteChannelUseCase
import com.example.new_practice.domain.usecase.GetChannelsInfoUseCase
import com.example.new_practice.domain.usecase.GetEpgByChannelIdUseCase
import com.example.new_practice.domain.usecase.GetEpgsUseCase
import com.example.new_practice.domain.usecase.SearchChannelsUseCase

class AppViewModelFactory(context: Context): ViewModelProvider.Factory {

    private val channelRepository = ChannelRepositoryImpl.getInstance(context = context)
    private val epgRepository = EpgRepositoryImpl.getInstance(context = context)

    private val changeFavoriteChannelUseCase: ChangeFavoriteChannelUseCase =
        ChangeFavoriteChannelUseCase(channelRepository = channelRepository)
    private val getChannelsInfoUseCase: GetChannelsInfoUseCase =
        GetChannelsInfoUseCase(channelRepository = channelRepository)
    private val getEpgByChannelIdUseCase: GetEpgByChannelIdUseCase =
        GetEpgByChannelIdUseCase(epgRepository = epgRepository)
    private val getEpgsUseCase: GetEpgsUseCase =
        GetEpgsUseCase(epgRepository = epgRepository)
    private val searchChannelsUseCase: SearchChannelsUseCase =
        SearchChannelsUseCase(channelRepository = channelRepository)

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when(modelClass) {
            ChannelViewModel::class.java -> ChannelViewModel(
                changeFavoriteChannelUseCase = changeFavoriteChannelUseCase,
                getChannelsInfoUseCase = getChannelsInfoUseCase,
                getEpgsUseCase = getEpgsUseCase,
                searchChannelsUseCase = searchChannelsUseCase
            ) as T
            VideoViewModel::class.java -> VideoViewModel(
                getChannelsInfoUseCase = getChannelsInfoUseCase,
                getEpgByChannelIdUseCase = getEpgByChannelIdUseCase
            ) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
}