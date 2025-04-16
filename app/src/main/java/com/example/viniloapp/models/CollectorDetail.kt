package com.example.viniloapp.models

data class CollectorDetail(
    val id: Int,
    val collector: Collector,
    val comments: List<Comment>,
    val favoritePerformers: List<Performer>,
    val collectorAlbums: List<CollectorAlbum>
)
