package com.example.viniloapp.network

import android.content.Context
import android.util.LruCache
import com.example.viniloapp.models.Collector
import com.example.viniloapp.models.CollectorDetail

class CacheManager(context: Context) {
    companion object {
        var instance: CacheManager? = null
        private var cacheCollectorsMiliseconds: Long = 0L
        private val cacheDurationMillis = 60_000L
        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: CacheManager(context).also {
                instance = it
            }
        }
    }

    private var collectorDetails: LruCache<Int, CollectorDetail> = LruCache(3)
    fun cacheCollectorDetails(collectorId: Int, collector: CollectorDetail){
        if (collectorDetails[collectorId] == null){
            collectorDetails.put(collectorId, collector)
        }
    }
    fun getCollectorDetails(collectorId: Int) : CollectorDetail? {
        return if (collectorDetails[collectorId]!=null) collectorDetails[collectorId]!! else null
    }

    private var collectors: List<Collector>? = null
    fun cacheCollectors(collectors: List<Collector>){
        cacheCollectorsMiliseconds = System.currentTimeMillis()
        this.collectors = collectors
    }

    fun getCollectors() : List<Collector>? {
        val now = System.currentTimeMillis()
        return if (now - cacheCollectorsMiliseconds < cacheDurationMillis) {
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


}