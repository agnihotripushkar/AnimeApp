package com.devpush.animeapp.features.details.ui

import com.devpush.animeapp.data.local.entities.AnimeDataEntity

sealed interface DetailsScreenUiState {
    data object Loading : DetailsScreenUiState
    data class Success(val animeData: AnimeDataEntity) : DetailsScreenUiState
    data class Error(val message: String?, val exception: Throwable? = null) : DetailsScreenUiState
    data object Idle : DetailsScreenUiState
}