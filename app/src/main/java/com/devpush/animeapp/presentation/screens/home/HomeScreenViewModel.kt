package com.devpush.animeapp.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpush.animeapp.domian.model.AnimeData
import com.devpush.animeapp.domian.repository.KitsuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel( private val repository: KitsuRepository): ViewModel() {

    private var _anime = MutableStateFlow<AnimeData?>(null)
    val anime = _anime.asStateFlow()

    fun fetchAnime(id: Int) {
        viewModelScope.launch {
            _anime.update { repository.getAnime(id) }
        }
    }



}