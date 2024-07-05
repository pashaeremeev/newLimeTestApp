package com.example.new_practice.storage.entities

import androidx.room.Entity
import java.util.Objects

@Entity(tableName = "channels", primaryKeys = ["id"])
class Channel(
    val id: Int,
    val name: String,
    val image: String,
    var isFavorite: Boolean,
    val stream: String
) {

    override fun hashCode(): Int {
        return Objects.hash(id, name, image, stream)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Channel

        if (id != other.id) return false
        if (name != other.name) return false
        if (image != other.image) return false
        if (isFavorite != other.isFavorite) return false
        if (stream != other.stream) return false

        return true
    }
}