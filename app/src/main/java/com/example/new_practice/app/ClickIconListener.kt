package com.example.new_practice.app

import com.example.new_practice.data.storage.entities.Channel
import com.example.new_practice.domain.models.ChannelModel

interface ClickIconListener {
    operator fun invoke(channel: ChannelModel)
}