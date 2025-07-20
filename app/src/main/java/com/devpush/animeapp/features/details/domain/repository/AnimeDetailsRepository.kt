package com.devpush.animeapp.features.details.domain.repository

import com.devpush.animeapp.core.network.NetworkResult
import com.devpush.animeapp.features.details.data.remote.responsebody.GenreResponse
import com.devpush.animeapp.features.trending.data.remote.responsebody.AnimeResponseDto

interface AnimeDetailsRepository {

    suspend fun getAnime(id: Int): NetworkResult<AnimeResponseDto>

    suspend fun getAnimeGenres(id: Int): NetworkResult<GenreResponse>
}