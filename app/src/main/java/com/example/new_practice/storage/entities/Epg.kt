package com.example.new_practice.storage.entities

import androidx.room.Entity

@Entity(tableName = "epgs", primaryKeys = ["id"])
class Epg(
    var id: Int,
    var channelId: Int,
    var timeStart: Int,
    var timeStop: Int,
    var title: String)