package com.devpush.animeapp.features.archived.ui

import com.devpush.animeapp.data.local.entities.AnimeDataEntity

data class ArchivedAnimeUiState(
    val isLoading: Boolean = false,
    val animes: List<AnimeDataEntity> = emptyList(),
    val error: String? = null
)