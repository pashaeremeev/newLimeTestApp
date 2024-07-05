package com.example.new_practice.repos

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.new_practice.storage.entities.Channel
import com.example.new_practice.storage.AppDatabase
import com.example.new_practice.storage.RoomInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ChannelRepo(context: Context) {
    //private val preferencesRepo: PreferencesRepo
    private val appDatabase: AppDatabase

    val searchFlow: MutableStateFlow<String> = MutableStateFlow("")
    val currentChannelId: MutableStateFlow<Int?> = MutableStateFlow(null)

    fun setSearchFilter(text: String) {
        searchFlow.value = text
    }

    init {
        //preferencesRepo = PreferencesRepo(context)
        appDatabase = RoomInstance.getInstance(context)
    }

    fun saveChannels(channels: List<Channel>) {
        CoroutineScope(Dispatchers.IO).launch {
            val oldChannelList = appDatabase.channelsDao().getChannelsNow()
            val newChannelList = mutableListOf<Channel>()
            for (i in channels.indices) {
                newChannelList.add(channels[i])
                val channel: Channel? = oldChannelList.firstOrNull { it.id == channels[i].id }
                if (channel != null) {
                    newChannelList[i].isFavorite = channel.isFavorite
                }
            }
            appDatabase.channelsDao().saveChannels(newChannelList)
        }
    }

    fun changeFav(channelId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.channelsDao().changeFav(channelId)
        }
    }

    val channels: LiveData<List<Channel>>
        get() {
            return appDatabase.channelsDao().getChannels()
        }

    val channelsFlow: Flow<List<Channel>> = appDatabase.channelsDao().getChannelsFlow()

    fun getById(id: Int): LiveData<Channel?> {
        return appDatabase.channelsDao().getChannelById(id).map { return@map it.firstOrNull() }
    }

    companion object {
        //private const val KEY_CHANNEL = "channel"
        private var channelRepo: ChannelRepo? = null
        fun getInstance(context: Context?): ChannelRepo {
            if (channelRepo == null) {
                channelRepo = ChannelRepo(context!!)
            }
            return channelRepo!!
        }
    }
}