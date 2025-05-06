package com.example.viniloapp.models

data class AlbumDetail(
    val id: Int,
    val name: String,
    val cover: String,
    val releaseDate: String,
    val description: String,
    val genre: String,
    val recordLabel: String,
    val tracks: List<Track>
)

data class Track(
    val id: Int,
    val name: String,
    val duration: String
) 