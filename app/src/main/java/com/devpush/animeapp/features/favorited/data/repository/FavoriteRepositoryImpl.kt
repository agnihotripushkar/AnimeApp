package com.devpush.animeapp.features.favorited.data.repository

import com.devpush.animeapp.data.local.dao.TrendingAnimeDao
import com.devpush.animeapp.data.local.entities.AnimeDataEntity
import com.devpush.animeapp.features.favorited.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow

class FavoriteRepositoryImpl(
    private val trendingAnimeDao: TrendingAnimeDao
): FavoriteRepository {

    override fun getFavoriteAnimes(): Flow<List<AnimeDataEntity>> {
        return trendingAnimeDao.getFavoriteAnimes()
    }
}