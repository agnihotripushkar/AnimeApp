package com.devpush.animeapp.features.trending.ui

import com.devpush.animeapp.domian.model.AnimeData

sealed interface TrendingAnimeUiState {
    data object Loading : TrendingAnimeUiState
    data class Success(val animeList: List<AnimeData>) : TrendingAnimeUiState
    data class Error(val message: String?, val exception: Throwable? = null) : TrendingAnimeUiState
    data object Idle : TrendingAnimeUiState
}