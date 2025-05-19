package com.example.viniloapp.models

data class Artist(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val birthDate: String,
    val albums: List<Album> = emptyList(),
    val performerPrizes: List<PerformerPrize> = emptyList()
)

data class PerformerPrize(
    val id: Int,
    val premiationDate: String
) 