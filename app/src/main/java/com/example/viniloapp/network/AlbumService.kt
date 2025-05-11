package com.example.viniloapp.network

import com.example.viniloapp.MyApplication
import com.example.viniloapp.models.Album
import com.example.viniloapp.models.AlbumDetail
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AlbumService(
    private val networkServiceAdapter: NetworkServiceAdapter =
        NetworkServiceAdapter.getInstance(MyApplication.getAppContext())
) {

    suspend fun getAlbums(): List<Album> {
        val json = networkServiceAdapter.get("albums")
        val type = object : TypeToken<List<Album>>() {}.type
        return Gson().fromJson(json, type)
    }

    suspend fun getAlbumDetail(albumId: Int): AlbumDetail {
        val json = networkServiceAdapter.get("albums/$albumId")
        return Gson().fromJson(json, AlbumDetail::class.java)
    }

    suspend fun createAlbum(jsonBody: JSONObject) {
        networkServiceAdapter.post("albums", jsonBody)
    }
}