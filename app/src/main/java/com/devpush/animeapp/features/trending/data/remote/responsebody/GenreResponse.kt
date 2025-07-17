package com.devpush.animeapp.features.trending.data.remote.responsebody

import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(
    val data: List<GenreData>
)

@Serializable
data class GenreData(
    val id: String,
    val type: String,
    val attributes: GenreAttributes
)

@Serializable
data class GenreAttributes(
    val name: String
)
