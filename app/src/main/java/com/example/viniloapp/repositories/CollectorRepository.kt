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
        val potentialResponse = CacheManager.getInstance(application.applicationContext).getCollectorDetails(collectorId)
        if (potentialResponse == null) {
            Log.d("Cache Strategy", "Getting collector $collectorId from network")
            val collectorDetail = collectorService.getCollectorDetail(collectorId)
            Log.d("Cache strategy", "Caching collector $collectorId")
            CacheManager.getInstance(application.applicationContext).cacheCollectorDetails(collectorId, collectorDetail)
            return collectorDetail
        }
        return potentialResponse
    }

    suspend fun getCollectors(): List<Collector> {
        val potentialResponse = CacheManager.getInstance(application.applicationContext).getCollectors()
        if (potentialResponse === null) {
            Log.d("Cache Strategy", "Getting collectors from network")
            val collectors = collectorService.getCollectors()
            Log.d("Cache Strategy", "Caching collectors")
            CacheManager.getInstance(application.applicationContext).cacheCollectors(collectors)
            return collectors
        }
        return potentialResponse
    }

}