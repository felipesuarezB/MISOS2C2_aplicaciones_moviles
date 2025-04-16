package com.example.viniloapp.network

import com.android.volley.VolleyError
import com.example.viniloapp.MyApplication
import com.example.viniloapp.models.Collector
import com.example.viniloapp.models.CollectorDetail

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

    suspend fun getCollectorDetail(collectorId: Int): CollectorDetail {
        return try {
            var collector: CollectorDetail? = null
            var error: VolleyError? = null

            networkServiceAdapter.getCollectorDetail(
                collectorId,
                onComplete = { detail ->
                    collector = detail
                },
                onError = { volleyError ->
                    error = volleyError
                }
            )

            if (error != null) {
                throw Exception(error?.message ?: "Error desconocido al obtener detalle del coleccionista")
            }

            collector ?: throw Exception("No se encontró el coleccionista")
        } catch (e: Exception) {
            throw e
        }
    }
}