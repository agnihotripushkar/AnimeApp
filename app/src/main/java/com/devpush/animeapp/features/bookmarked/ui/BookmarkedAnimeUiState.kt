package com.devpush.animeapp.features.bookmarked.ui

data class BookmarkedAnimeUiState(
    val isLoading: Boolean = false,
    val animes: List<String> = emptyList(), // Replace String with actual Anime model
    val error: String? = null
)