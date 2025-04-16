package com.example.viniloapp.models

data class CollectorAlbum(
    val id: Int,
    val price: Double,
    val status: String,
    val album: Album
)