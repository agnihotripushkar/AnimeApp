package com.devpush.animeapp.features.trending.domain.repository

import com.devpush.animeapp.core.network.NetworkResult
import com.devpush.animeapp.data.local.entities.AnimeDataEntity
import com.devpush.animeapp.features.trending.data.remote.responsebody.TrendingAnimeListResponse
import kotlinx.coroutines.flow.Flow

interface TrendingAnimeRepository {

    suspend fun getTrendingAnime(): NetworkResult<TrendingAnimeListResponse>

    fun getTrendingAnimeFromDb(): Flow<List<AnimeDataEntity>>

    suspend fun saveTrendingAnime(animeList: List<AnimeDataEntity>)

}