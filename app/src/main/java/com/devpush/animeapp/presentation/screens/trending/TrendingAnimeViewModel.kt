package com.devpush.animeapp.presentation.screens.trending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpush.animeapp.domian.model.AnimeData
import com.devpush.animeapp.domian.repository.KitsuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TrendingAnimeViewModel(private val repository: KitsuRepository):ViewModel() {

    private var _animeData = MutableStateFlow<List<AnimeData>>(emptyList())
    val animeData = _animeData.asStateFlow()

    init {
        viewModelScope.launch {
            _animeData.update { repository.getTrendingAnime() }
        }
    }
}