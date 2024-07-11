package com.example.new_practice.data.storage

import com.example.new_practice.data.storage.entities.Epg
import kotlinx.coroutines.flow.Flow

interface EpgStorage {

    fun saveEpgs(channels: List<Epg>)

    fun getEpgs(): Flow<List<Epg>>
}