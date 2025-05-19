package com.example.viniloapp.network

import com.example.viniloapp.models.Prize
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PrizeService {
    @POST("prizes")
    fun createPrize(@Body prize: Map<String, String>): Call<Prize>
}
