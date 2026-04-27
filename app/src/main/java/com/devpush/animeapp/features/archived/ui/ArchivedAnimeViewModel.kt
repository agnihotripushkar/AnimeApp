package com.devpush.animeapp.features.archived.ui

import android.content.ContentValues.TAG
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpush.animeapp.features.archived.domain.repository.ArchivedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class ArchivedAnimeViewModel(
    private val archivedRepository: ArchivedRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ArchivedState(isLoading = true))
    val state = _state.asStateFlow()

    private val _events = Channel<ArchivedEvent>()
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            archivedRepository.getArchivedAnimes()
                .map { animeList -> ArchivedState(isLoading = false, animeList = animeList) }
                .catch { e ->
                    Timber.e(e, "Error fetching archived animes")
                    emit(ArchivedState(isLoading = false, error = e.localizedMessage ?: "An error occurred"))
                }
                .collect { newState -> _state.value = newState }
        }
    }

    fun onAction(action: ArchivedAction) {
        when (action) {
            is ArchivedAction.ToggleArchive -> toggleArchive(action.animeId, action.isCurrentlyArchived)
            is ArchivedAction.AnimeClick -> viewModelScope.launch {
                _events.send(ArchivedEvent.NavigateToDetail(action.animeId))
            }
        }
    }

    private fun toggleArchive(animeId: String, isCurrentlyArchived: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                archivedRepository.updateArchivedStatus(animeId, !isCurrentlyArchived)
            } catch (e: Exception) {
                Timber.tag(TAG).e(e, "Failed to toggle archived for $animeId")
            }
        }
    }
}
