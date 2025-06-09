package com.devpush.animeapp.features.details.ui

import com.devpush.animeapp.domian.model.AnimeData

sealed interface DetailsScreenUiState {
    data object Loading : DetailsScreenUiState
    data class Success(val animeData: AnimeData) : DetailsScreenUiState
    data class Error(val message: String?, val exception: Throwable? = null) : DetailsScreenUiState
    data object Idle : DetailsScreenUiState // Optional: for an initial, pre-loading state
}