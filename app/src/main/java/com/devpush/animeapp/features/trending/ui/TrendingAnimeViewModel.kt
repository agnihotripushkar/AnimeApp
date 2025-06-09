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

class TrendingAnimeViewModel(private val repository: TrendingAnimeRepository): ViewModel() {

    private var _uiState = MutableStateFlow<TrendingAnimeUiState>(TrendingAnimeUiState.Idle)
    val uiState = _uiState.asStateFlow()

    init {
        fetchTrendingAnime()
    }
    private fun fetchTrendingAnime() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = TrendingAnimeUiState.Loading
            when (val apiResult = repository.getTrendingAnime()) {
                is NetworkResult.Loading -> {
                    _uiState.value = TrendingAnimeUiState.Loading
                }
                is NetworkResult.Success -> {
                    try {
                        val animeDataList = apiResult.data.toModel()
                        _uiState.value = TrendingAnimeUiState.Success(animeDataList)

                    } catch (e: Exception) {
                        Timber.tag(TAG).e(e, "Error mapping DTO to domain model")
                        _uiState.value = TrendingAnimeUiState.Error("Error processing data", e)
                    }
                }
                is NetworkResult.Error -> {
                    Timber.tag(TAG)
                        .e(apiResult.exception, "getTrendingAnime failed: ${apiResult.message}")
                    _uiState.value = TrendingAnimeUiState.Error(apiResult.message, apiResult.exception)
                }

            }
        }
    }

    fun retryFetchTrendingAnime(){
        fetchTrendingAnime()
    }
}