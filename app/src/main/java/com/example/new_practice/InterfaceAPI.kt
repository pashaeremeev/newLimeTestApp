package com.example.new_practice

import com.example.new_practice.json.ChannelJsonModel
import retrofit2.Call
import retrofit2.http.GET

interface InterfaceAPI {
    @get:GET("9c0a9a5f5abea9d3ef39")
    val data: Call<ChannelJsonModel?>?
}