package com.devpush.animeapp.features.trending.ui

import android.content.ContentValues.TAG
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpush.animeapp.core.network.NetworkResult
import com.devpush.animeapp.features.trending.domain.repository.TrendingAnimeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class TrendingAnimeViewModel(private val repository: TrendingAnimeRepository) : ViewModel() {

    private var _uiState = MutableStateFlow<TrendingAnimeUiState>(TrendingAnimeUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _selectedCategory = MutableStateFlow(AnimeCategory.ALL)
    val selectedCategory = _selectedCategory.asStateFlow()

    init {
        fetchTrendingAnimeFromServer(animeCategory = AnimeCategory.TRENDING)
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

    fun onCategorySelected(category: AnimeCategory) {
        _selectedCategory.value = category
        fetchTrendingAnimeFromServer(animeCategory = category)
    }

    private fun fetchTrendingAnimeFromServer(animeCategory: AnimeCategory) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = TrendingAnimeUiState.Loading
            val apiResult = when (animeCategory) {
                AnimeCategory.TRENDING -> repository.getTrendingAnime()
                AnimeCategory.ALL -> repository.getAllAnime()
            }
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
        fetchTrendingAnimeFromServer(animeCategory = _selectedCategory.value)
    }

    fun starAnime(animeId: String, isCurrentlyFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updateFavoriteStatus(animeId, !isCurrentlyFavorite)
                Timber.tag(TAG).d("Toggled favorite status for anime: %s to %s", animeId, !isCurrentlyFavorite)
            } catch (e: Exception) {
                Timber.tag(TAG).e(e, "Failed to toggle favorite status for anime: %s", animeId)
                // Optionally, notify the UI about the error
            }
        }
    }

    fun archiveAnime(animeId: String, isCurrentlyArchived: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updateArchivedStatus(animeId, !isCurrentlyArchived)
            } catch (e: Exception) {
                Timber.tag(TAG).e(e, "Failed to toggle favorite status for anime: %s", animeId)
                // Optionally, notify the UI about the error
            }
        }
    }
}