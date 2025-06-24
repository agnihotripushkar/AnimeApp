package com.devpush.animeapp.features.archived.domain.repository

import com.devpush.animeapp.data.local.entities.AnimeDataEntity
import kotlinx.coroutines.flow.Flow

interface ArchivedRepository {

    fun getArchivedAnimes(): Flow<List<AnimeDataEntity>>

    suspend fun updateArchivedStatus(animeId: String, isArchived: Boolean)

}