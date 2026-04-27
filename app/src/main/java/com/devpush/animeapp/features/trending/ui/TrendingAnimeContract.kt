package com.devpush.animeapp.features.trending.ui

import com.devpush.animeapp.core.presentation.UiText
import com.devpush.animeapp.data.local.entities.AnimeDataEntity

data class TrendingAnimeState(
    val animeList: List<AnimeDataEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: UiText? = null
)

sealed interface TrendingAnimeAction {
    data object Retry : TrendingAnimeAction
    data class StarAnime(val animeId: String, val isCurrentlyFavorite: Boolean) : TrendingAnimeAction
    data class ArchiveAnime(val animeId: String, val isCurrentlyArchived: Boolean) : TrendingAnimeAction
}

sealed interface TrendingAnimeEvent
