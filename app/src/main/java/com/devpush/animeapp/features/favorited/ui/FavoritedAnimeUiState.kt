package com.devpush.animeapp.features.favorited.ui

import com.devpush.animeapp.data.local.entities.AnimeDataEntity

data class FavoritedAnimeUiState(
    val isLoading: Boolean = false,
    val animes: List<AnimeDataEntity> = emptyList(),
    val error: String? = null
)
