package com.devpush.animeapp.features.details.ui

import com.devpush.animeapp.data.local.entities.AnimeDataEntity
import com.devpush.animeapp.features.details.data.remote.responsebody.GenreData

data class DetailsState(
    val animeData: AnimeDataEntity? = null,
    val genres: List<GenreData> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

sealed interface DetailsAction {
    data class LoadAnime(val id: Int) : DetailsAction
}

sealed interface DetailsEvent {
    data object NavigateBack : DetailsEvent
}
