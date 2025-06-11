package com.devpush.animeapp.features.favorited.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavoritedAnimeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritedAnimeUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.value = FavoritedAnimeUiState(isLoading = false,
            animes = listOf("Placeholder Favorited Anime 1", "Placeholder Favorited Anime 2"))
    }
}