package com.example.new_practice.app.json

import com.example.new_practice.data.storage.entities.Channel
import com.example.new_practice.data.storage.entities.Epg
import com.example.new_practice.domain.models.ChannelModel
import com.example.new_practice.domain.models.EpgModel
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

    fun createChannel(): ChannelModel {
        return ChannelModel(
            id,
            name,
            image,
            false,
            stream
        )
    }

    fun createEpg(): EpgModel {
        val epgJson: EpgJson = epg[0]
        return EpgModel(
            epgJson.id,
            id,
            epgJson.timeStart,
            epgJson.timeStop,
            epgJson.title
        )
    }
}