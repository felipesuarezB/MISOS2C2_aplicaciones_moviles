package com.example.viniloapp.network

import android.util.Log
import com.android.volley.VolleyError
import com.example.viniloapp.MyApplication
import com.example.viniloapp.models.Collector
import com.example.viniloapp.models.CollectorDetail
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CollectorService(
    private val networkServiceAdapter: NetworkServiceAdapter =
        NetworkServiceAdapter.getInstance(MyApplication.getAppContext())
) {

    suspend fun getCollectors(): List<Collector> {
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

        return collectorsList
    }

    suspend fun getCollectorDetail(collectorId: Int): CollectorDetail {
        return suspendCoroutine<CollectorDetail> { continuation ->
            networkServiceAdapter.getCollectorDetail(
                collectorId,
                onComplete = { detail: CollectorDetail ->
                    continuation.resume(
                        detail
                    )

                },
                onError = { volleyError ->
                    continuation.resumeWithException(volleyError)
                }
            )
        }
    }
}