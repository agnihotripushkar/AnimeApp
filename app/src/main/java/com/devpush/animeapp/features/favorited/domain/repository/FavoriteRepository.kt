package com.devpush.animeapp.features.favorited.domain.repository

import com.devpush.animeapp.data.local.entities.AnimeDataEntity
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    fun getFavoriteAnimes(): Flow<List<AnimeDataEntity>>

    suspend fun updateFavoriteStatus(animeId: String, isFavorite: Boolean)
}