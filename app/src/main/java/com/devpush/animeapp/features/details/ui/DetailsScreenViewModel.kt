package com.devpush.animeapp.features.details.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpush.animeapp.core.network.NetworkResult
import com.devpush.animeapp.features.details.domain.repository.AnimeDetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class DetailsScreenViewModel(private val repository: AnimeDetailsRepository) : ViewModel() {

    private val _state = MutableStateFlow(DetailsState())
    val state = _state.asStateFlow()

    private val _events = Channel<DetailsEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: DetailsAction) {
        when (action) {
            is DetailsAction.LoadAnime -> loadAnime(action.id)
        }
    }

    private fun loadAnime(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true, error = null) }
            val animeResult = repository.getAnime(id)
            val genresResult = repository.getAnimeGenres(id)

            when {
                animeResult is NetworkResult.Success && genresResult is NetworkResult.Success -> {
                    _state.update {
                        it.copy(
                            animeData = animeResult.data.toModel(),
                            genres = genresResult.data.data,
                            isLoading = false
                        )
                    }
                }
                animeResult is NetworkResult.Error -> {
                    Timber.e("getAnime for id $id failed: ${animeResult.message}")
                    _state.update { it.copy(isLoading = false, error = animeResult.message) }
                }
                genresResult is NetworkResult.Error -> {
                    Timber.e("getAnimeGenres for id $id failed: ${genresResult.message}")
                    _state.update { it.copy(isLoading = false, error = genresResult.message) }
                }
                else -> _state.update { it.copy(isLoading = false) }
            }
        }
    }
}
