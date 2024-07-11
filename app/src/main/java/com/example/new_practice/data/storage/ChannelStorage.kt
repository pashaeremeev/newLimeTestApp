package com.example.new_practice.data.storage

import com.example.new_practice.data.storage.entities.Channel
import kotlinx.coroutines.flow.Flow

interface ChannelStorage {

    fun saveChannels(channels: List<Channel>)

    fun getChannels(): Flow<List<Channel>>
}