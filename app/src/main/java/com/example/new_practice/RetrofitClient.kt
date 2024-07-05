package com.example.new_practice

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {

    private val baseUrl = "https://api.npoint.io/"
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: InterfaceAPI
        get() = retrofit.create(InterfaceAPI::class.java)

    companion object {
        private var retrofitClient: RetrofitClient? = null
        val instance: RetrofitClient?
            get() {
                if (retrofitClient == null) {
                    retrofitClient = RetrofitClient()
                }
                return retrofitClient
            }
    }
}