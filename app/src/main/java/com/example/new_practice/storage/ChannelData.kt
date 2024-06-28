package com.example.new_practice.storage

import androidx.room.Entity

@Entity(tableName = "channels", primaryKeys = ["id"])
data class ChannelData(
    val id: Int,
    val name: String,
    val image: String,
    val isFav: Boolean,
    val stream: String
)
