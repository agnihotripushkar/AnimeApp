package com.devpush.animeapp.features.archived.ui

import com.devpush.animeapp.data.local.entities.AnimeDataEntity

data class ArchivedState(
    val animeList: List<AnimeDataEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface ArchivedAction {
    data class ToggleArchive(val animeId: String, val isCurrentlyArchived: Boolean) : ArchivedAction
    data class AnimeClick(val animeId: String) : ArchivedAction
}

sealed interface ArchivedEvent {
    data class NavigateToDetail(val animeId: String) : ArchivedEvent
}
