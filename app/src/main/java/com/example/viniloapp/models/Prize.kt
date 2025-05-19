package com.example.viniloapp.models

data class Prize(
    val name: String,
    val description: String,
    val organization: String,
    val imageUrl: String? = null // Opcional, solo para uso local
)
