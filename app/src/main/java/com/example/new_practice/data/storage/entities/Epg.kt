package com.example.new_practice.data.storage.entities

import androidx.room.Entity
import com.example.new_practice.domain.models.EpgModel

@Entity(tableName = "epgs", primaryKeys = ["id"])
class Epg(
    var id: Int,
    var channelId: Int,
    var timeStart: Int,
    var timeStop: Int,
    var title: String
) {
    fun toEpgModel(): EpgModel {
         return EpgModel(
             this.id,
             this.channelId,
             this.timeStart,
             this.timeStop,
             this.title
         )
    }
}