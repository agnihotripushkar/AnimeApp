package com.devpush.animeapp.features.favorited.ui

import com.devpush.animeapp.data.local.entities.AnimeDataEntity

data class FavoritedState(
    val animeList: List<AnimeDataEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface FavoritedAction {
    data class ToggleFavorite(val animeId: String, val isCurrentlyFavorite: Boolean) : FavoritedAction
    data class AnimeClick(val animeId: String) : FavoritedAction
}

sealed interface FavoritedEvent {
    data class NavigateToDetail(val animeId: String) : FavoritedEvent
}
