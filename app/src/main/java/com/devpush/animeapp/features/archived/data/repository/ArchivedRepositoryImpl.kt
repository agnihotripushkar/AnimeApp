package com.devpush.animeapp.features.archived.data.repository

import com.devpush.animeapp.data.local.dao.TrendingAnimeDao
import com.devpush.animeapp.data.local.entities.AnimeDataEntity
import com.devpush.animeapp.features.archived.domain.repository.ArchivedRepository
import kotlinx.coroutines.flow.Flow

class ArchivedRepositoryImpl(
    private val trendingAnimeDao: TrendingAnimeDao
): ArchivedRepository {
    override fun getArchivedAnimes(): Flow<List<AnimeDataEntity>> {
        return trendingAnimeDao.getArchivedAnimes()
    }

    override suspend fun updateArchivedStatus(animeId: String, isArchived: Boolean) {
        return trendingAnimeDao.updateArchivedStatus(animeId, isArchived)
    }

}