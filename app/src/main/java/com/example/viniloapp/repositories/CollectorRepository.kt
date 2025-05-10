package com.example.viniloapp.repositories

import android.app.Application
import android.util.Log
import com.example.viniloapp.models.Collector
import com.example.viniloapp.models.CollectorDetail
import com.example.viniloapp.network.CacheManager
import com.example.viniloapp.network.CollectorService

class CollectorRepository(val application: Application) {

    private val collectorService: CollectorService = CollectorService()

    suspend fun getCollectorDetail(collectorId: Int): CollectorDetail {
        val cacheManager = CacheManager.getInstance(application.applicationContext)

        val potentialResponse = cacheManager.getCollectorDetails(collectorId)
        if (potentialResponse == null) {
            Log.d("Cache Strategy", "Getting collector $collectorId from network")
            val collectorDetail = collectorService.getCollectorDetail(collectorId)
            Log.d("Cache strategy", "Caching collector $collectorId")
            cacheManager.cacheCollectorDetails(collectorId, collectorDetail)
            return collectorDetail
        }
        return potentialResponse
    }

    suspend fun getCollectors(): List<Collector> {
        val cacheManager = CacheManager.getInstance(application.applicationContext)

        val potentialResponse = cacheManager.getCollectors()
        if (potentialResponse === null) {
            Log.d("Cache Strategy", "Getting collectors from network")
            val collectors = collectorService.getCollectors()
            Log.d("Cache Strategy", "Caching collectors")
            cacheManager.cacheCollectors(collectors)
            return collectors
        }
        return potentialResponse
    }

}