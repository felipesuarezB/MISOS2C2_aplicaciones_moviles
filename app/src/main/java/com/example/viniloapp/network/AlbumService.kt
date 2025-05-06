package com.example.viniloapp.network

import android.util.Log
import com.android.volley.VolleyError
import com.example.viniloapp.MyApplication
import com.example.viniloapp.models.AlbumDetail
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AlbumService(
    private val networkServiceAdapter: NetworkServiceAdapter =
        NetworkServiceAdapter.getInstance(MyApplication.getAppContext())
) {
    suspend fun getAlbumDetail(albumId: Int): AlbumDetail {
        return suspendCoroutine<AlbumDetail> { continuation ->
            networkServiceAdapter.getAlbumDetail(
                albumId,
                onComplete = { detail: AlbumDetail ->
                    continuation.resume(detail)
                },
                onError = { volleyError ->
                    continuation.resumeWithException(volleyError)
                }
            )
        }
    }
} 