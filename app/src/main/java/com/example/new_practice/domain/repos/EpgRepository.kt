package com.example.new_practice.domain.repos

import com.example.new_practice.domain.models.EpgModel
import kotlinx.coroutines.flow.Flow

interface EpgRepository {

    fun saveEpgs(epgs: ArrayList<EpgModel>)

    fun getEpgs(): Flow<List<EpgModel>>
}