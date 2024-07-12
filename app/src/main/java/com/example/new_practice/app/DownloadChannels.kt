package com.example.new_practice.app

import android.util.Log
import com.example.new_practice.json.ChannelJson
import com.example.new_practice.json.ChannelJsonModel
import com.example.new_practice.domain.models.ChannelModel
import com.example.new_practice.domain.models.EpgModel
import com.example.new_practice.domain.repos.ChannelRepository
import com.example.new_practice.domain.repos.EpgRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class DownloadChannels {

    companion object {
        fun downloadChannels(
            channelRepository: ChannelRepository,
            epgRepository: EpgRepository
            //callback: (Boolean) -> Unit
        ) {
            val scheduler: ScheduledExecutorService? = Executors.newSingleThreadScheduledExecutor()
            scheduler?.scheduleAtFixedRate({
                RetrofitClient.instance?.api?.data
                    ?.enqueue(object : Callback<ChannelJsonModel?> {
                        override fun onResponse(
                            call: Call<ChannelJsonModel?>,
                            response: Response<ChannelJsonModel?>
                        ) {
                            if (response.isSuccessful) {
                                val channelJsons: ArrayList<ChannelJson> =
                                    response.body()!!.channelJsons
                                val channels: ArrayList<ChannelModel> = ArrayList()
                                val epgs: ArrayList<EpgModel> = ArrayList()
                                for (i in channelJsons.indices) {
                                    val channelJson: ChannelJson = channelJsons[i]
                                    val channel: ChannelModel = channelJson.createChannel()
                                    channels.add(channel)
                                    val epg: EpgModel = channelJson.createEpg()
                                    epgs.add(epg)
                                }
                                val channelsFlowFromRepository: Flow<List<ChannelModel>> =
                                    channelRepository.getChannels()
                                var channelsFromRepository = listOf<ChannelModel>()
                                CoroutineScope(Dispatchers.IO).launch {
                                    channelsFromRepository = channelsFlowFromRepository.first()
                                }
                                for (i in channels.indices) {
                                    if (channels[i].id == channelsFromRepository.getOrNull(i)?.id
                                        && channels[i].isFavorite != channelsFromRepository.getOrNull(i)?.isFavorite
                                    ) {
                                        channels[i].isFavorite = channelsFromRepository[i].isFavorite
                                    }
                                }
                                if (channelsFromRepository.toString() == channels.toString()) {
                                    //callback.invoke(false)
                                }
                                channelRepository.saveChannels(channels)
                                epgRepository.saveEpgs(epgs)
                            }
                            //callback.invoke(true)
                        }

                        override fun onFailure(call: Call<ChannelJsonModel?>, t: Throwable) {
                            //callback.invoke(false)
                        }
                    })
                Log.d("TAG", "1")
            }, 0, REFRESH_TIME.toLong(), TimeUnit.MILLISECONDS)
        }

        private const val REFRESH_TIME = 900000
    }
}