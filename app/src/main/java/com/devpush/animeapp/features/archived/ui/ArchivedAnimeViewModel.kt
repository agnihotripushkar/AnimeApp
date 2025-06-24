package com.devpush.animeapp.features.archived.ui

import android.content.ContentValues.TAG
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpush.animeapp.features.archived.domain.repository.ArchivedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

class ArchivedAnimeViewModel(
    private val archivedRepository: ArchivedRepository
) : ViewModel() {

    val uiState: StateFlow<ArchivedAnimeUiState> =
        archivedRepository.getArchivedAnimes()
            .map { animeList -> ArchivedAnimeUiState(isLoading = false, animes = animeList) }
            .catch { e ->
                Timber.e(e, "Error fetching Archived animes")
                emit(
                    ArchivedAnimeUiState(
                        isLoading = false,
                        error = e.localizedMessage ?: "An error occurred"
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ArchivedAnimeUiState(isLoading = true)
            )

    fun archiveAnime(animeId: String, isCurrentlyArchived: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                archivedRepository.updateArchivedStatus(animeId, !isCurrentlyArchived)
            } catch (e: Exception) {
                Timber.tag(TAG).e(e, "Failed to toggle favorite status for anime: %s", animeId)
                // Optionally, notify the UI about the error
            }
        }
    }


    fun toggleArchivedStatus(animeId: String, isCurrentlyArchived: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Since this screen only shows favorites, toggling means un-favoriting.
                // Or, if it's a general toggle, then it works as is.
                // For this context, !isCurrentlyFavorite will typically be false.
                //repository.updateFavoriteStatus(animeId, !isCurrentlyFavorite)
                Timber.d(
                    "Toggled favorite status for anime: %s to %s",
                    animeId,
                    !isCurrentlyArchived
                )
            } catch (e: Exception) {
                Timber.e(e, "Failed to toggle favorite status for anime: %s", animeId)
                // Optionally, update UI state with error
                // _uiState.value = _uiState.value.copy(error = "Failed to update favorite status")
            }
        }
    }
}