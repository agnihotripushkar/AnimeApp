package com.devpush.animeapp.features.home.ui

import com.devpush.animeapp.core.presentation.UiText
import com.devpush.animeapp.data.local.entities.AnimeDataEntity

data class HomeState(
    val animeList: List<AnimeDataEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: UiText? = null
)

sealed interface HomeAction {
    data object Retry : HomeAction
    data class StarAnime(val animeId: String, val isCurrentlyFavorite: Boolean) : HomeAction
    data class ArchiveAnime(val animeId: String, val isCurrentlyArchived: Boolean) : HomeAction
}

sealed interface HomeEvent
