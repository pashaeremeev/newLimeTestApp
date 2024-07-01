package com.example.new_practice.repos

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.new_practice.Channel
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.lang.Exception

class ChannelRepo(context: Context?) {
    private val preferencesRepo: PreferencesRepo

    init {
        preferencesRepo = PreferencesRepo(context)
        channels
    }

    fun saveChannels(channels: ArrayList<Channel>) {
        val channelsJson: String = Gson().toJson(channels)
        preferencesRepo.save(channelsJson, KEY_CHANNEL)
        channelMutableLiveData.postValue(channels)
    }

    val channels: Unit
        get() {
            val listJsonString: String? = preferencesRepo.get(KEY_CHANNEL)
            val type = object : TypeToken<ArrayList<Channel?>?>() {}.type
            val channels: ArrayList<Channel> = try {
                Gson().fromJson(listJsonString, type)
            } catch (e: Exception) {
                arrayListOf()
            }
            channelMutableLiveData.postValue(channels ?: ArrayList<Channel>())
        }

    val favChannels: ArrayList<Channel>
        get() {
            val listJsonString: String? = preferencesRepo.get(KEY_CHANNEL)
            val type = object : TypeToken<ArrayList<Channel?>?>() {}.type
            val channels: ArrayList<Channel> = Gson().fromJson(listJsonString, type)
            val favChannels: ArrayList<Channel> = ArrayList<Channel>()
            for (i in channels.indices) {
                if (channels[i].isFavorite) {
                    favChannels.add(channels[i])
                }
            }
            return if (favChannels == null) ArrayList() else channels
        }

    fun getById(id: Int): Channel? {
        val channels: ArrayList<Channel> = channelMutableLiveData.value!!
        for (i in channels.indices) {
            val channel: Channel = channels[i]
            if (channel.id === id) {
                return channel
            }
        }
        return null
    }

    companion object {
        private const val KEY_CHANNEL = "channel"
        private val channelMutableLiveData: MutableLiveData<ArrayList<Channel>> =
            MutableLiveData<ArrayList<Channel>>(
                ArrayList<Channel>()
            )
        private var channelRepo: ChannelRepo? = null
        val liveData: LiveData<ArrayList<Channel>>
            get() = channelMutableLiveData
        fun getInstance(context: Context?): ChannelRepo {
            if (channelRepo == null) {
                channelRepo = ChannelRepo(context!!)
            }
            return channelRepo!!
        }
    }
}