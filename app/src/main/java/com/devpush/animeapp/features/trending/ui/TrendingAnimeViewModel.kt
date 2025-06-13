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

    init {
        fetchTrendingAnimeFromServer(initialLoad = true)
        viewModelScope.launch(Dispatchers.IO) {
            repository.getTrendingAnimeFromDb().collect { animeList ->
                if (animeList.isNotEmpty()) {
                    _uiState.value = TrendingAnimeUiState.Success(animeList)
                } else {
                    if (_uiState.value !is TrendingAnimeUiState.Error && _uiState.value !is TrendingAnimeUiState.Loading) {
                        // DB became empty (e.g. after a refresh that returned nothing),
                        // and we are not in an error state or currently loading from server.
                        _uiState.value =
                            TrendingAnimeUiState.Success(emptyList()) // Or a specific "No data" state
                    }
                }
            }
        }

    }

    private fun fetchTrendingAnimeFromServer(initialLoad: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = TrendingAnimeUiState.Loading
            when (val apiResult = repository.getTrendingAnime()) {
                is NetworkResult.Loading -> {
                }

                is NetworkResult.Success -> {
                    val animeDataList = apiResult.data.toModel()
                    if (animeDataList.isEmpty() &&
                        (_uiState.value as? TrendingAnimeUiState.Success)?.animeList?.isEmpty() == true) {
                        _uiState.value = TrendingAnimeUiState.Success(emptyList())
                    }
                    else{
                        repository.saveTrendingAnime(animeDataList)
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
        fetchTrendingAnimeFromServer(initialLoad = false)
    }

    fun starAnime(animeId: String) {
        Timber.tag(TAG).d("Starring anime: %s", animeId)
        // In a real scenario, you would call a use case or repository here
        // to update the starred status of the anime.
    }

    fun archiveAnime(animeId: String) {
        Timber.tag(TAG).d("Archiving anime: %s", animeId)
        // In a real scenario, you would call a use case or repository here
        // to update the archived status of the anime.
    }
}