package com.example.viniloapp.repositories

import android.app.Application
import com.example.viniloapp.network.PrizeService
import org.json.JSONObject

class PrizeRepository(val application: Application) {
    private val prizeService = PrizeService()

    suspend fun createPrize(name: String, description: String, organization: String) {
        val jsonBody = JSONObject().apply {
            put("name", name)
            put("description", description)
            put("organization", organization)
        }
        prizeService.createPrize(jsonBody)
    }
}
