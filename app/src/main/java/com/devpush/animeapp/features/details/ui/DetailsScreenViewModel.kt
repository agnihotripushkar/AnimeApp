package com.devpush.animeapp.features.details.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpush.animeapp.core.network.NetworkResult
import com.devpush.animeapp.features.details.domain.repository.AnimeDetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class DetailsScreenViewModel(private val repository: AnimeDetailsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailsScreenUiState>(DetailsScreenUiState.Loading)
    val uiState: StateFlow<DetailsScreenUiState> = _uiState.asStateFlow()

    fun fetchAnime(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = DetailsScreenUiState.Loading
            val animeResult = repository.getAnime(id)
            val genresResult = repository.getAnimeGenres(id)

            if (animeResult is NetworkResult.Success && genresResult is NetworkResult.Success) {
                val animeData = animeResult.data.toModel()
                val genresData = genresResult.data.data
                _uiState.value = DetailsScreenUiState.Success(animeData, genresData)
            } else if (animeResult is NetworkResult.Error) {
                Timber.e("getAnime for id $id failed: ${animeResult.message}")
                _uiState.value =
                    DetailsScreenUiState.Error(animeResult.message, animeResult.exception)
            } else if (genresResult is NetworkResult.Error) {
                Timber.e("getAnimeGenres for id $id failed: ${genresResult.message}")
                _uiState.value =
                    DetailsScreenUiState.Error(genresResult.message, genresResult.exception)
            }
        }
    }
}