package com.example.viniloapp.network

import com.example.viniloapp.models.Prize
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

package com.example.viniloapp.network

import com.example.viniloapp.models.Prize
import org.json.JSONObject

class PrizeService {
    private val networkServiceAdapter = NetworkServiceAdapter.instance!!

    suspend fun createPrize(jsonBody: JSONObject) {
        networkServiceAdapter.post("prizes", jsonBody)
    }
}
