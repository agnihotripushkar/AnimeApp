package com.devpush.animeapp.presentation.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpush.animeapp.domian.model.AnimeData
import com.devpush.animeapp.domian.repository.KitsuRepository
import com.devpush.animeapp.network.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsScreenViewModel( private val repository: KitsuRepository): ViewModel() {

    private val _uiState = MutableStateFlow<NetworkResult<AnimeData?>>(NetworkResult.Loading) // Default to Loading or an Idle state
    val uiState: StateFlow<NetworkResult<AnimeData?>> = _uiState.asStateFlow()


    fun fetchAnime(id: Int) {
        viewModelScope.launch {
            _uiState.value = NetworkResult.Loading // Set to loading before making the call
            val result = repository.getAnime(id)
            _uiState.value = result
        }
    }

    fun retryFetchAnime(id: Int) {
        fetchAnime(id)
    }

    fun clearAnimeDetails() {
        _uiState.value = NetworkResult.Loading // Or an Idle state if you have one
        // _anime.value = null // If using the separate state
    }

}