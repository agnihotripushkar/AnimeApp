package com.devpush.animeapp.features.trending.ui

import com.devpush.animeapp.data.local.entities.AnimeDataEntity

enum class AnimeCategory {
    TRENDING,
    ALL
}

sealed interface TrendingAnimeUiState {
    data object Loading : TrendingAnimeUiState
    data class Success(val animeList: List<AnimeDataEntity>) : TrendingAnimeUiState
    data class Error(val message: String?, val exception: Throwable? = null) : TrendingAnimeUiState
    data object Idle : TrendingAnimeUiState
}