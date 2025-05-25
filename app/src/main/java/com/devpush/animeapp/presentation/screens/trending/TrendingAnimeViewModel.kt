package com.devpush.animeapp.presentation.screens.trending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpush.animeapp.domian.model.AnimeData
import com.devpush.animeapp.domian.repository.KitsuRepository
import com.devpush.animeapp.network.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TrendingAnimeViewModel(private val repository: KitsuRepository):ViewModel() {

    private var _uiState = MutableStateFlow<NetworkResult<List<AnimeData>>>(NetworkResult.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchTrendingAnime()
    }

    private fun fetchTrendingAnime(){
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = NetworkResult.Loading

            val result = repository.getTrendingAnime()
            _uiState.value = result

        }
    }

    fun retryFetchTrendingAnime(){
        fetchTrendingAnime()
    }
}