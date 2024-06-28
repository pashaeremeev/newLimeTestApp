package com.example.new_practice

import com.example.new_practice.json.ChannelJsonModel
import retrofit2.Call
import retrofit2.http.GET

interface InterfaceAPI {
    @get:GET("rKkyTS")
    val data: Call<ChannelJsonModel?>?
}