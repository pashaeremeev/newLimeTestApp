package com.example.new_practice.app

import com.example.new_practice.data.storage.entities.Channel
import com.example.new_practice.domain.models.ChannelModel

interface ClickChannelListener {
    operator fun invoke(channel: ChannelModel?)
}