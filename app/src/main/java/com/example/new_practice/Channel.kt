package com.example.new_practice

import java.util.Objects

class Channel(
    val id: Int,
    val name: String,
    val image: String,
    var isFavorite: Boolean,
    val stream: String
) {

    override fun equals(obj: Any?): Boolean {
        return super.equals(obj)
    }

    override fun hashCode(): Int {
        return Objects.hash(id, name, image, stream)
    }
}