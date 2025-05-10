package com.example.viniloapp.repositories

import android.app.Application
import android.util.Log
import com.example.viniloapp.models.Album
import com.example.viniloapp.models.AlbumDetail
import com.example.viniloapp.network.AlbumService
import com.example.viniloapp.network.CacheManager
import org.json.JSONObject

class AlbumRepository(val application: Application) {
    private val albumService: AlbumService = AlbumService()

    suspend fun getAlbumDetail(albumId: Int): AlbumDetail {
        val cacheManager = CacheManager.getInstance(application.applicationContext)

        val possibleResponse = cacheManager.getAlbumDetail(albumId)
        if (possibleResponse == null) {
            Log.d("Cache Strategy", "Getting album $albumId from network")
            val albumDetail = albumService.getAlbumDetail(albumId)
            Log.d("Cache strategy", "Caching album $albumId")
            cacheManager.cacheAlbumDetail(albumId, albumDetail)
            return albumDetail
        }
        return possibleResponse
    }

    suspend fun getAlbums(): List<Album> {
        val cacheManager = CacheManager.getInstance(application.applicationContext)

        val possibleResponse = cacheManager.getAlbums()
        if (possibleResponse == null) {
            Log.d("Cache Strategy", "Getting albums from network")
            val albums = albumService.getAlbums()
            Log.d("Cache strategy", "Caching albums")
            cacheManager.cacheAlbums(albums)
            return albums
        }
        return possibleResponse
    }

    suspend fun createAlbum(jsonBody: JSONObject) {
        val cacheManager = CacheManager.getInstance(application.applicationContext)
        Log.d("Cache Strategy", "Cleaning albums cache")
        cacheManager.clearAlbumCache()
        return albumService.createAlbum(jsonBody)

    }

}