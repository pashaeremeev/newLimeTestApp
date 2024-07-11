package com.example.new_practice.domain.models

class ChannelModel(
    val id: Int,
    val name: String,
    val image: String,
    var isFavorite: Boolean,
    val stream: String)
