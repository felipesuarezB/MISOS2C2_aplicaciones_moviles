package com.example.viniloapp.network

import com.example.viniloapp.MyApplication
import com.example.viniloapp.models.Artist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class ArtistService(
    private val networkServiceAdapter: NetworkServiceAdapter =
        NetworkServiceAdapter.getInstance(MyApplication.getAppContext())
) {

    suspend fun getArtists(): List<Artist> {
        val json = networkServiceAdapter.get("musicians")
        val type = object : TypeToken<List<Artist>>() {}.type
        return Gson().fromJson(json, type)
    }

    suspend fun getArtistDetail(artistId: Int): Artist {
        val json = networkServiceAdapter.get("musicians/$artistId")
        return Gson().fromJson(json, Artist::class.java)
    }
} 