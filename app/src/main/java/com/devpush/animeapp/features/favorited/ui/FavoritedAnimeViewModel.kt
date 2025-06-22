package com.devpush.animeapp.features.favorited.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpush.animeapp.features.favorited.domain.repository.FavoriteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

class FavoritedAnimeViewModel(
    private val repository: FavoriteRepository
) : ViewModel() {

    val uiState: StateFlow<FavoritedAnimeUiState> =
        repository.getFavoriteAnimes()
            .map { animeList -> FavoritedAnimeUiState(isLoading = false, animes = animeList) }
            .catch { e ->
                Timber.e(e, "Error fetching favorite animes")
                emit(
                    FavoritedAnimeUiState(
                        isLoading = false,
                        error = e.localizedMessage ?: "An error occurred"
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = FavoritedAnimeUiState(isLoading = true)
            )

    fun toggleFavoriteStatus(animeId: String, isCurrentlyFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Since this screen only shows favorites, toggling means un-favoriting.
                // Or, if it's a general toggle, then it works as is.
                // For this context, !isCurrentlyFavorite will typically be false.
                //repository.updateFavoriteStatus(animeId, !isCurrentlyFavorite)
                Timber.d(
                    "Toggled favorite status for anime: %s to %s",
                    animeId,
                    !isCurrentlyFavorite
                )
            } catch (e: Exception) {
                Timber.e(e, "Failed to toggle favorite status for anime: %s", animeId)
                // Optionally, update UI state with error
                // _uiState.value = _uiState.value.copy(error = "Failed to update favorite status")
            }
        }
    }
}