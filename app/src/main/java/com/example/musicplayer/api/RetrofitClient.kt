package com.example.musicplayer.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    

    private const val BASE_URL = "https://mocki.io/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    val musicApiService: MusicApiService by lazy {
        retrofit.create(MusicApiService::class.java)
    }
}