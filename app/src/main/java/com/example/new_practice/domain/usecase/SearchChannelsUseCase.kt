package com.example.new_practice.domain.usecase

import com.example.new_practice.domain.models.ChannelModel
import com.example.new_practice.domain.repos.ChannelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class SearchChannelsUseCase(
    private val channelRepository: ChannelRepository
) {

    fun launch(searchParam: Flow<String>): Flow<List<ChannelModel>> {
        return channelRepository.getChannels().combine(searchParam) {
            channels, filter ->
            var filteredChannelsList: ArrayList<ChannelModel> = arrayListOf()
            if (filter.isNotEmpty()) {
                for (channel in channels) {
                    if (channel.name.contains(filter, ignoreCase = true)) {
                        filteredChannelsList.add(channel)
                    }
                }
            }
            return@combine filteredChannelsList
        }
    }
}