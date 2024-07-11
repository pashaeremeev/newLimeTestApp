package com.example.new_practice.domain.models

class EpgModel(
    var id: Int,
    var channelId: Int,
    var timeStart: Int,
    var timeStop: Int,
    var title: String)