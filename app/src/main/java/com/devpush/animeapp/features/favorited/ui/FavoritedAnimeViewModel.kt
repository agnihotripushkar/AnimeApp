package com.devpush.animeapp.features.favorited.ui

import android.content.ContentValues.TAG
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpush.animeapp.features.favorited.domain.repository.FavoriteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class FavoritedAnimeViewModel(
    private val repository: FavoriteRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FavoritedState(isLoading = true))
    val state = _state.asStateFlow()

    private val _events = Channel<FavoritedEvent>()
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            repository.getFavoriteAnimes()
                .map { animeList -> FavoritedState(isLoading = false, animeList = animeList) }
                .catch { e ->
                    Timber.e(e, "Error fetching favorite animes")
                    emit(FavoritedState(isLoading = false, error = e.localizedMessage ?: "An error occurred"))
                }
                .collect { newState -> _state.value = newState }
        }
    }

    fun onAction(action: FavoritedAction) {
        when (action) {
            is FavoritedAction.ToggleFavorite -> toggleFavorite(action.animeId, action.isCurrentlyFavorite)
            is FavoritedAction.AnimeClick -> viewModelScope.launch {
                _events.send(FavoritedEvent.NavigateToDetail(action.animeId))
            }
        }
    }

    private fun toggleFavorite(animeId: String, isCurrentlyFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updateFavoriteStatus(animeId, !isCurrentlyFavorite)
            } catch (e: Exception) {
                Timber.tag(TAG).e(e, "Failed to toggle favorite for $animeId")
            }
        }
    }
}
