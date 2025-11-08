package com.devpush.animeapp.features.home.ui

import android.content.ContentValues.TAG
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpush.animeapp.core.network.NetworkResult
import com.devpush.animeapp.features.trending.domain.repository.TrendingAnimeRepository
import com.devpush.animeapp.features.trending.ui.AnimeCategory
import com.devpush.animeapp.features.trending.ui.TrendingAnimeUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel(private val repository: TrendingAnimeRepository): ViewModel() {

    private var _uiState = MutableStateFlow<TrendingAnimeUiState>(TrendingAnimeUiState.Idle)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAnimeFromDb().collect { animeList ->
                if (animeList.isNotEmpty()) {
                    _uiState.value = TrendingAnimeUiState.Success(animeList)
                } else {
                    if (_uiState.value !is TrendingAnimeUiState.Error && _uiState.value !is TrendingAnimeUiState.Loading) {
                        _uiState.value =
                            TrendingAnimeUiState.Success(emptyList())
                    }
                }
            }
        }
    }

    private fun fetchTrendingAnimeFromServer() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = TrendingAnimeUiState.Loading
            val apiResult = repository.getAllAnime()
            when (apiResult) {
                is NetworkResult.Loading -> {
                }

                is NetworkResult.Success -> {
                    val animeDataList = apiResult.data.toModel()
                    if (animeDataList.isEmpty() &&
                        (_uiState.value as? TrendingAnimeUiState.Success)?.animeList?.isEmpty() == true) {
                        _uiState.value = TrendingAnimeUiState.Success(emptyList())
                    }
                    else{
                        repository.saveAnime(animeDataList)
                    }
                }

                is NetworkResult.Error -> {
                    Timber.tag(TAG)
                        .e(
                            apiResult.exception,
                            "fetchTrendingAnimeFromServer failed: ${apiResult.message}"
                        )
                    if ((_uiState.value as? TrendingAnimeUiState.Success)?.animeList?.isEmpty() != false) {
                        _uiState.value =
                            TrendingAnimeUiState.Error(apiResult.message, apiResult.exception)
                    } else {
                        Timber.tag(TAG).w(
                            apiResult.exception,
                            "fetchTrendingAnimeFromServer failed but showing stale data: ${apiResult.message}"
                        )

                    }
                }
            }
        }
    }

    fun retryFetchTrendingAnime() {
        fetchTrendingAnimeFromServer()
    }

    fun starAnime(animeId: String, isCurrentlyFavorite: Boolean) {
        // Optimistically update the UI
        val currentState = _uiState.value
        if (currentState is TrendingAnimeUiState.Success) {
            val updatedList = currentState.animeList.map { anime ->
                if (anime.id == animeId) {
                    anime.copy(isFavorite = !isCurrentlyFavorite)
                } else {
                    anime
                }
            }
            _uiState.value = TrendingAnimeUiState.Success(updatedList)
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updateFavoriteStatus(animeId, !isCurrentlyFavorite)
                Timber.tag(TAG).d("Toggled favorite status for anime: %s to %s", animeId, !isCurrentlyFavorite)
            } catch (e: Exception) {
                Timber.tag(TAG).e(e, "Failed to toggle favorite status for anime: %s", animeId)
                // Optionally, revert UI update or notify the user
                // For now, we'll rely on the database flow to correct any discrepancies eventually
            }
        }
    }

    fun archiveAnime(animeId: String, isCurrentlyArchived: Boolean) {
        // Optimistically update the UI
        val currentState = _uiState.value
        if (currentState is TrendingAnimeUiState.Success) {
            val updatedList = currentState.animeList.map { anime ->
                if (anime.id == animeId) {
                    anime.copy(isArchived = !isCurrentlyArchived)
                } else {
                    anime
                }
            }
            _uiState.value = TrendingAnimeUiState.Success(updatedList)
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updateArchivedStatus(animeId, !isCurrentlyArchived)
                Timber.tag(TAG).d("Toggled archived status for anime: %s to %s", animeId, !isCurrentlyArchived)
            } catch (e: Exception) {
                Timber.tag(TAG).e(e, "Failed to toggle archived status for anime: %s", animeId)
                // Optionally, notify the UI about the error
            }
        }
    }
}