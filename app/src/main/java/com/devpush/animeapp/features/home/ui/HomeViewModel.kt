package com.devpush.animeapp.features.home.ui

import android.content.ContentValues.TAG
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpush.animeapp.R
import com.devpush.animeapp.core.network.NetworkResult
import com.devpush.animeapp.core.presentation.UiText
import com.devpush.animeapp.features.trending.domain.repository.TrendingAnimeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel(private val repository: TrendingAnimeRepository) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _events = Channel<HomeEvent>()
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAnimeFromDb().collect { animeList ->
                if (animeList.isNotEmpty()) {
                    _state.update { it.copy(animeList = animeList, isLoading = false, error = null) }
                } else if (!_state.value.isLoading && _state.value.error == null) {
                    fetchFromServer()
                }
            }
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.Retry -> fetchFromServer()
            is HomeAction.StarAnime -> starAnime(action.animeId, action.isCurrentlyFavorite)
            is HomeAction.ArchiveAnime -> archiveAnime(action.animeId, action.isCurrentlyArchived)
        }
    }

    private fun fetchFromServer() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = repository.getAllAnime()) {
                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    val list = result.data.toModel()
                    if (list.isNotEmpty()) {
                        repository.saveAnime(list)
                    } else if (_state.value.animeList.isEmpty()) {
                        _state.update { it.copy(isLoading = false) }
                    }
                }
                is NetworkResult.Error -> {
                    Timber.tag(TAG).e(result.exception, "fetchFromServer: ${result.message}")
                    if (_state.value.animeList.isEmpty()) {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.message?.let { msg -> UiText.DynamicString(msg) }
                                    ?: UiText.StringResource(R.string.an_error_occurred)
                            )
                        }
                    } else {
                        _state.update { it.copy(isLoading = false) }
                        Timber.tag(TAG).w(result.exception, "fetch failed, showing stale data")
                    }
                }
            }
        }
    }

    private fun starAnime(animeId: String, isCurrentlyFavorite: Boolean) {
        _state.update { current ->
            current.copy(
                animeList = current.animeList.map { anime ->
                    if (anime.id == animeId) anime.copy(isFavorite = !isCurrentlyFavorite) else anime
                }
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updateFavoriteStatus(animeId, !isCurrentlyFavorite)
            } catch (e: Exception) {
                Timber.tag(TAG).e(e, "Failed to toggle favorite for $animeId")
            }
        }
    }

    private fun archiveAnime(animeId: String, isCurrentlyArchived: Boolean) {
        _state.update { current ->
            current.copy(
                animeList = current.animeList.map { anime ->
                    if (anime.id == animeId) anime.copy(isArchived = !isCurrentlyArchived) else anime
                }
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updateArchivedStatus(animeId, !isCurrentlyArchived)
            } catch (e: Exception) {
                Timber.tag(TAG).e(e, "Failed to toggle archived for $animeId")
            }
        }
    }
}
