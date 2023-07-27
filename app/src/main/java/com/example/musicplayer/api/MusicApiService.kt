package com.example.musicplayer.api

import com.example.musicplayer.models.MusicData
import retrofit2.Response
import retrofit2.http.GET

interface MusicApiService {
    @GET("v1/f27fbefc-d775-4aee-8d65-30f76f1f7109")
    suspend fun getMusicData(): Response<MusicData>
    suspend fun getMusicCategories(): MusicData

}