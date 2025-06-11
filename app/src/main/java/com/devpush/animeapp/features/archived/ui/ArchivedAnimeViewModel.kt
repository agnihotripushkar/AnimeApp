package com.devpush.animeapp.features.archived.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ArchivedAnimeViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(ArchivedAnimeUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.value = ArchivedAnimeUiState(isLoading = false,
            animes = listOf("Placeholder Archived Anime 1", "Placeholder Archived Anime 2"))
    }
}