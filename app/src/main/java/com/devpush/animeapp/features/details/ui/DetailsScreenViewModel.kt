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

    // Default to Loading or an Idle state
    private val _uiState = MutableStateFlow<DetailsScreenUiState>(DetailsScreenUiState.Loading)
    val uiState: StateFlow<DetailsScreenUiState> = _uiState.asStateFlow()

    fun fetchAnime(id: Int) {

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = DetailsScreenUiState.Loading // Set to loading before making the call
            when (val apiResult = repository.getAnime(id)) { // Make the call and process immediately
                is NetworkResult.Loading -> {
                    _uiState.value = DetailsScreenUiState.Loading
                }

                is NetworkResult.Success -> {
                    // Successfully fetched DTO, now map it to domain model
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