package com.example.viniloapp.network

import com.example.viniloapp.models.Album
import com.example.viniloapp.MyApplication
import com.android.volley.VolleyError

class AlbumService(
    private val networkServiceAdapter: NetworkServiceAdapter =
        NetworkServiceAdapter.getInstance(MyApplication.getAppContext())
) {

    suspend fun getAlbums(): List<Album> {
        var albumsList: List<Album> = emptyList()
        var error: VolleyError? = null

        networkServiceAdapter.getAlbums(
            onComplete = { albums ->
                albumsList = albums
            },
            onError = { volleyError ->
                error = volleyError
            }
        )

        if (error != null) {
            throw Exception(error?.message ?: "Error desconocido")
        }

        return albumsList
    }
} 