package com.example.new_practice.domain.usecase

import com.example.new_practice.domain.models.EpgModel
import com.example.new_practice.domain.repos.EpgRepository
import kotlinx.coroutines.flow.Flow

class GetEpgsUseCase(private val epgRepository: EpgRepository) {

    fun launch(): Flow<List<EpgModel>> {
        return epgRepository.getEpgs()
    }
}