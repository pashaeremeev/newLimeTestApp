package com.example.new_practice.domain.usecase

import com.example.new_practice.domain.models.ChannelModel
import com.example.new_practice.domain.repos.ChannelRepository
import kotlinx.coroutines.flow.Flow

class GetChannelsInfoUseCase(
    private val channelRepository: ChannelRepository
) {

    fun launch(): Flow<List<ChannelModel>> {
        return channelRepository.getChannels()
    }
}