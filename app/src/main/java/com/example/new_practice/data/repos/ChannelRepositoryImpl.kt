package com.example.new_practice.data.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.new_practice.domain.repos.ChannelRepository
import com.example.new_practice.data.storage.entities.Channel
import com.example.new_practice.data.storage.implDb.AppDatabase
import com.example.new_practice.domain.models.ChannelModel
import com.example.new_practice.app.network.DownloadChannels
import com.example.new_practice.domain.repos.EpgRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ChannelRepositoryImpl(
    private val appDatabase: AppDatabase,
    epgRepository: EpgRepository
) : ChannelRepository {

    init {
        DownloadChannels.downloadChannels(
            this, epgRepository
        )
    }

    override fun saveChannels(channels: List<ChannelModel>) {
        CoroutineScope(Dispatchers.IO).launch {
            val oldChannelList = appDatabase.channelsDao().getChannelsNow()
            val newChannelList = mutableListOf<Channel>()
            for (i in channels.indices) {
                var channel: Channel = fromChannelModelToChannel(channels[i])
                newChannelList.add(channel)
                channel = oldChannelList.firstOrNull { it.id == channels[i].id }!!
                if (channel != null) {
                    newChannelList[i].isFavorite = channel.isFavorite
                }
            }
            appDatabase.channelsDao().saveChannels(newChannelList)
        }
    }

    override fun changeFav(channelId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.channelsDao().changeFav(channelId)
        }
    }

    val channels: LiveData<List<ChannelModel>>
        get() {
            return appDatabase.channelsDao().getChannels().map { channelsList ->
                channelsList.map { channel ->
                    channel.toChannelModel()
                }
            }
        }

    //val channelsFlow: Flow<List<Channel>> = appDatabase.channelsDao().getChannelsFlow()

    override fun searchChannels(searchTextParam: String): Flow<List<ChannelModel>> {
        return appDatabase.channelsDao().searchChannels(searchTextParam).map { channelsList ->
            channelsList.map { channel ->
                channel.toChannelModel()
            }
        }
    }

    override fun getChannels(): Flow<List<ChannelModel>> {
        return appDatabase.channelsDao().getChannelsFlow().map { channelsList ->
            channelsList.map { channel ->
                channel.toChannelModel()
            }
        }
    }

    override fun getById(id: Int): LiveData<ChannelModel?> {
        return appDatabase.channelsDao().getChannelById(id).map {
            return@map it.firstOrNull()?.toChannelModel()
        }
    }

    private fun fromChannelModelToChannel(channel: ChannelModel): Channel {
        return Channel(
            channel.id,
            channel.name,
            channel.image,
            channel.isFavorite,
            channel.stream
        )
    }

    companion object {
        private var channelRepo: ChannelRepositoryImpl? = null
        fun getInstance(
            appDatabase: AppDatabase,
            epgRepository: EpgRepository
        ): ChannelRepositoryImpl {
            if (channelRepo == null) {
                channelRepo = ChannelRepositoryImpl(appDatabase, epgRepository)
            }
            return channelRepo!!
        }
    }
}