package com.example.new_practice.domain.usecase

import com.example.new_practice.domain.models.EpgModel
import com.example.new_practice.domain.repos.EpgRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetEpgByChannelIdUseCase(private val epgRepository: EpgRepository) {

    fun launch(channelId: Flow<Int?>): Flow<EpgModel> {
        return epgRepository.getEpgs().combine(flow = channelId) { epgs, channelId ->
            return@combine epgs.firstOrNull { it.channelId == channelId }!!
        }
    }
}