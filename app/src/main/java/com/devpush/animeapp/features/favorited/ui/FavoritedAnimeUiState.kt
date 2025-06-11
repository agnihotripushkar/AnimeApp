package com.devpush.animeapp.features.favorited.ui

data class FavoritedAnimeUiState(
    val isLoading: Boolean = false,
    val animes: List<String> = emptyList(), // Replace String with actual Anime model
    val error: String? = null
)
