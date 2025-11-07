package com.devpush.animeapp.features.recommendation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpush.animeapp.data.local.entities.AnimeDataEntity
import com.devpush.animeapp.features.trending.domain.repository.TrendingAnimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RecommendationViewModel(
    private val animeRepository: TrendingAnimeRepository
) : ViewModel() {

    private val recommendationEngine = RecommendationEngine()

    private val _recommendedAnime = MutableStateFlow<List<AnimeDataEntity>>(emptyList())
    val recommendedAnime: StateFlow<List<AnimeDataEntity>> = _recommendedAnime

    fun getRecommendations() {
        viewModelScope.launch {
            val allAnime = animeRepository.getAnimeFromDb().first()
            val favoriteAnime = allAnime.filter { it.isFavorite }
            _recommendedAnime.value = recommendationEngine.getRecommendations(allAnime, favoriteAnime)
        }
    }
}
