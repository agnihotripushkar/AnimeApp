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
            when (val apiResult = repository.getAnime(id)) {
                is NetworkResult.Loading -> {
                    _uiState.value = DetailsScreenUiState.Loading
                }

                is NetworkResult.Success -> {
                    val animeData = apiResult.data.toModel()
                    _uiState.value = DetailsScreenUiState.Success(animeData)
                }

                is NetworkResult.Error -> {
                    Timber.e("getAnime for id $id failed: ${apiResult.message}")
                    _uiState.value =
                        DetailsScreenUiState.Error(apiResult.message, apiResult.exception)
                }
            }
        }
    }

}