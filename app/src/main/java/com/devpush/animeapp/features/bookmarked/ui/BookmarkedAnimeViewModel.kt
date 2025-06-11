package com.devpush.animeapp.features.bookmarked.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class BookmarkedAnimeViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(BookmarkedAnimeUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.value = BookmarkedAnimeUiState(isLoading = false,
            animes = listOf("Placeholder Bookmarked Anime 1", "Placeholder Bookmarked Anime 2"))
    }
}