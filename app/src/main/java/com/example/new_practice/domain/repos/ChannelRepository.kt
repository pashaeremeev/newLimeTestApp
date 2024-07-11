package com.example.new_practice.domain.repos

import androidx.lifecycle.LiveData
import com.example.new_practice.data.storage.entities.Channel
import com.example.new_practice.domain.models.ChannelModel
import kotlinx.coroutines.flow.Flow

interface ChannelRepository {

    fun setSearchFilter(text: String)

    fun saveChannels(channels: List<ChannelModel>)

    fun changeFav(channelId: Int)

    fun getChannels(): Flow<List<ChannelModel>>

    fun getById(id: Int): LiveData<ChannelModel?>

    fun searchChannels(searchTextParam: String): Flow<List<ChannelModel>>
}