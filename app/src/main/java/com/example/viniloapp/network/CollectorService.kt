package com.example.viniloapp.network

import com.android.volley.VolleyError
import com.example.viniloapp.MyApplication
import com.example.viniloapp.models.Collector

class CollectorService {
    private val networkServiceAdapter = NetworkServiceAdapter.getInstance(MyApplication.getAppContext())

    suspend fun getCollectors(): List<Collector> {
        return try {
            var collectorsList: List<Collector> = emptyList()
            var error: VolleyError? = null

            networkServiceAdapter.getCollectors(
                onComplete = { collectors ->
                    collectorsList = collectors
                },
                onError = { volleyError ->
                    error = volleyError
                }
            )

            if (error != null) {
                throw Exception(error?.message ?: "Error desconocido")
            }

            collectorsList
        } catch (e: Exception) {
            throw e
        }
    }
}