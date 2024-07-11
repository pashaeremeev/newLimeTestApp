package com.example.new_practice.domain.usecase

import com.example.new_practice.domain.models.ChannelModel
import com.example.new_practice.domain.repos.ChannelRepository

class ChangeFavoriteChannelUseCase(
    private val channelRepository: ChannelRepository
) {

    fun launch(channelId: Int) {
        channelRepository.changeFav(channelId)
    }
}