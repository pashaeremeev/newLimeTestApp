package com.example.new_practice

import com.example.new_practice.storage.entities.Channel

interface ClickChannelListener {
    operator fun invoke(channel: Channel?)
}