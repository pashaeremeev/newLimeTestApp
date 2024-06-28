package com.example.new_practice.json

import com.example.new_practice.Channel
import com.example.new_practice.Epg
import com.example.practice.EpgJson

class ChannelJson(
    var id: Int,
    var name: String,
    var image: String,
    epg: ArrayList<EpgJson>,
    stream: String
) {
    private var epg: ArrayList<EpgJson>
    var stream: String

    init {
        this.epg = epg
        this.stream = stream
    }

    fun getEpg(): ArrayList<EpgJson> {
        return epg
    }

    fun setEpg(epg: ArrayList<EpgJson>) {
        this.epg = epg
    }

    fun createChannel(): Channel {
        return Channel(
            id,
            name,
            image,
            false,
            stream
        )
    }

    fun createEpg(): Epg {
        val epgJson: EpgJson = epg[0]
        return Epg(
            epgJson.id,
            id,
            epgJson.timeStart,
            epgJson.timeStop,
            epgJson.title
        )
    }
}