package com.devpush.animeapp.features.details.ui

import com.devpush.animeapp.data.local.entities.AnimeDataEntity
import com.devpush.animeapp.features.details.data.remote.responsebody.GenreData

sealed interface DetailsScreenUiState {
    data object Loading : DetailsScreenUiState
    data class Success(val animeData: AnimeDataEntity,
                       val genres: List<GenreData> = emptyList()) : DetailsScreenUiState
    data class Error(val message: String?, val exception: Throwable? = null) : DetailsScreenUiState
    data object Idle : DetailsScreenUiState
}