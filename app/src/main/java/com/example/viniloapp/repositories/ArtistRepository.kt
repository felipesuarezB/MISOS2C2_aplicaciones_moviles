package com.example.viniloapp.repositories

import android.app.Application
import android.util.Log
import com.example.viniloapp.models.Artist
import com.example.viniloapp.network.ArtistService
import com.example.viniloapp.network.CacheManager

class ArtistRepository(val application: Application) {
    private val artistService: ArtistService = ArtistService()

    suspend fun getArtistDetail(artistId: Int): Artist {
        val possibleResponse = CacheManager.getInstance(application.applicationContext).getArtistDetail(artistId)
        if (possibleResponse == null) {
            Log.d("Cache Strategy", "Getting artist $artistId from network")
            val artistDetail = artistService.getArtistDetail(artistId)
            Log.d("Cache strategy", "Caching artist $artistId")
            CacheManager.getInstance(application.applicationContext).cacheArtistDetail(artistId, artistDetail)
            return artistDetail
        }
        return possibleResponse
    }

    suspend fun getArtists(): List<Artist> {
        val possibleResponse = CacheManager.getInstance(application.applicationContext).getArtists()
        if (possibleResponse == null) {
            Log.d("Cache Strategy", "Getting artists from network")
            val artists = artistService.getArtists()
            Log.d("Cache strategy", "Caching artists")
            CacheManager.getInstance(application.applicationContext).cacheArtists(artists)
            return artists
        }
        return possibleResponse
    }
} 