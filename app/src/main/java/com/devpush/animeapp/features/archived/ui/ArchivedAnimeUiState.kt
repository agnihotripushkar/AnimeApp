package com.devpush.animeapp.features.archived.ui

data class ArchivedAnimeUiState(
    val isLoading: Boolean = false,
    val animes: List<String> = emptyList(), // Replace String with actual Anime model if needed
    val error: String? = null
)