package com.example.viniloapp.network

import com.example.viniloapp.MyApplication
import com.example.viniloapp.models.Album
import com.example.viniloapp.models.Collector
import com.example.viniloapp.models.CollectorAlbum
import com.example.viniloapp.models.CollectorDetail
import com.example.viniloapp.models.Comment
import com.example.viniloapp.models.Performer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CollectorService(
    private val networkServiceAdapter: NetworkServiceAdapter =
        NetworkServiceAdapter.getInstance(MyApplication.getAppContext())
) {

    suspend fun getCollectors(): List<Collector> {
        val json = networkServiceAdapter.get("collectors")
        val type = object : TypeToken<List<Collector>>() {}.type
        return Gson().fromJson(json, type)
    }

    suspend fun getCollectorDetail(
        collectorId: Int,
    ): CollectorDetail {
        val json = networkServiceAdapter.get("collectors/$collectorId")
        return Gson().fromJson(json, CollectorDetail::class.java)
    }
}