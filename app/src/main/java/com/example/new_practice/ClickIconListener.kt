package com.example.new_practice

import com.example.new_practice.storage.entities.Channel

interface ClickIconListener {
    operator fun invoke(channel: Channel)
}