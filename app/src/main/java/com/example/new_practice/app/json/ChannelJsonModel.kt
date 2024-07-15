package com.example.new_practice.app.json

class ChannelJsonModel(channelJsons: ArrayList<ChannelJson>) {
    private var channels: ArrayList<ChannelJson>

    init {
        channels = channelJsons
    }

    var channelJsons: ArrayList<ChannelJson>
        get() = channels
        set(channelJsons) {
            channels = channelJsons
        }
}