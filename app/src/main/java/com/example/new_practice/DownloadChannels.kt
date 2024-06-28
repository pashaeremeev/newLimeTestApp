package com.example.new_practice

import android.util.Log
import com.example.new_practice.json.ChannelJson
import com.example.new_practice.json.ChannelJsonModel
import com.example.new_practice.repos.ChannelRepo
import com.example.new_practice.repos.EpgRepo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class DownloadChannels {

    companion object {
        fun downloadChannels(
            channelRepo: ChannelRepo,
            epgRepo: EpgRepo,
            callback: Function1<Boolean?, Void?>
        ) {
            var scheduler: ScheduledExecutorService? = null
            scheduler = Executors.newSingleThreadScheduledExecutor()
            scheduler!!.scheduleAtFixedRate(Runnable {
                RetrofitClient.instance?.api?.data
                    ?.enqueue(object : Callback<ChannelJsonModel?> {
                        override fun onResponse(
                            call: Call<ChannelJsonModel?>?,
                            response: Response<ChannelJsonModel?>
                        ) {
                            if (response.isSuccessful) {
                                val channelJsons: ArrayList<ChannelJson> =
                                    response.body()!!.channelJsons
                                val channels: ArrayList<Channel> = ArrayList<Channel>()
                                val epgs: ArrayList<Epg> = ArrayList<Epg>()
                                for (i in channelJsons.indices) {
                                    val channelJson: ChannelJson = channelJsons[i]
                                    val channel: Channel = channelJson.createChannel()
                                    channels.add(channel)
                                    val epg: Epg = channelJson.createEpg()
                                    epgs.add(epg)
                                }
                                val channelsRepo: ArrayList<Channel>? =
                                    ChannelRepo.liveData.value

                                for (i in channels.indices) {
                                    if (channels[i].id == channelsRepo?.getOrNull(i)?.id
                                        && channels[i].isFavorite != channelsRepo.getOrNull(i)?.isFavorite
                                    ) {
                                        channels[i].isFavorite = channelsRepo[i].isFavorite
                                    }
                                }
                                if (channelsRepo.toString() == channels.toString()) {
                                    callback.invoke(false)
                                }
                                channelRepo.saveChannels(channels)
                                epgRepo.saveEpgs(epgs)
                            }
                            callback.invoke(true)
                        }

                        override fun onFailure(call: Call<ChannelJsonModel?>?, t: Throwable?) {
                            callback.invoke(false)
                        }
                    })
                Log.d("TAG", "1")
            }, 0, REFRESH_TIME.toLong(), TimeUnit.MILLISECONDS)
        }

        private const val REFRESH_TIME = 900000
    }
}