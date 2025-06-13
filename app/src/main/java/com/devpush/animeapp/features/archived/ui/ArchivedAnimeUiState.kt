package com.devpush.animeapp.features.archived.ui

data class ArchivedAnimeUiState(
    val isLoading: Boolean = false,
    val animes: List<String> = emptyList(),
    val error: String? = null
)