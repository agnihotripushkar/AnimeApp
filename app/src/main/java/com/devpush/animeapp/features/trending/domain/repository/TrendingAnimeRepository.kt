package com.devpush.animeapp.features.trending.domain.repository

import com.devpush.animeapp.core.network.NetworkResult
import com.devpush.animeapp.core.network.dto.TrendingAnimeListDto

interface TrendingAnimeRepository {

    suspend fun getTrendingAnime(): NetworkResult<TrendingAnimeListDto>

}