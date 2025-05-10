package com.example.viniloapp.network

import android.content.Context
import android.util.LruCache
import com.example.viniloapp.models.Album
import com.example.viniloapp.models.AlbumDetail
import com.example.viniloapp.models.Collector
import com.example.viniloapp.models.CollectorDetail

class CacheManager(context: Context) {
    companion object {
        var instance: CacheManager? = null
        private val cacheDurationMillis = 60_000L
        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: CacheManager(context).also {
                instance = it
            }
        }
    }

    // Collector cache
    private var collectorDetails: LruCache<Int, CollectorDetail> = LruCache(3)
    fun cacheCollectorDetails(collectorId: Int, collector: CollectorDetail){
        if (collectorDetails[collectorId] == null){
            collectorDetails.put(collectorId, collector)
        }
    }
    fun getCollectorDetails(collectorId: Int) : CollectorDetail? {
        return collectorDetails[collectorId]
    }

    private var cacheCollectorsMilliseconds: Long = 0L
    private var collectors: List<Collector>? = null
    fun cacheCollectors(collectors: List<Collector>){
        cacheCollectorsMilliseconds = System.currentTimeMillis()
        this.collectors = collectors
    }

    fun getCollectors() : List<Collector>? {
        val now = System.currentTimeMillis()
        return if (now - cacheCollectorsMilliseconds < cacheDurationMillis) {
            collectors
        } else {
            collectors = null
            null
        }
    }

    // TODO: Call when a new Collector is created
    fun clearCollectorsCache() {
        collectors = null
    }

    // Album cache
    private var albumDetail: LruCache<Int, AlbumDetail> = LruCache(3)
    fun cacheAlbumDetail(albumId: Int, album: AlbumDetail){
        if (albumDetail[albumId] == null){
            albumDetail.put(albumId, album)
        }
    }

    fun getAlbumDetail(albumId: Int):AlbumDetail? {
        return albumDetail[albumId]
    }

    private var cacheAlbumsMilliseconds: Long = 0L
    private var albums: List<Album>? = null
    fun cacheAlbums(albums: List<Album>){
        cacheAlbumsMilliseconds = System.currentTimeMillis()
        this.albums = albums
    }

    fun getAlbums() : List<Album>? {
        val now = System.currentTimeMillis()
        return if (now - cacheAlbumsMilliseconds < cacheDurationMillis) {
            albums
        } else {
            albums = null
            null
        }
    }

    fun clearAlbumCache() {
        albums = null;
    }

}