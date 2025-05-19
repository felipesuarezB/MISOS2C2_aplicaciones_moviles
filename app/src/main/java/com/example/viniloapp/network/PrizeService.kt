package com.example.viniloapp.network
import org.json.JSONObject

class PrizeService {
    private val networkServiceAdapter = NetworkServiceAdapter.instance!!

    suspend fun createPrize(jsonBody: JSONObject) {
        networkServiceAdapter.post("prizes", jsonBody)
    }
}
