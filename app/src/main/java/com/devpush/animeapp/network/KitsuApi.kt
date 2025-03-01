package com.devpush.animeapp.network

import com.devpush.animeapp.network.dto.AnimeResponseDto
import com.devpush.animeapp.network.dto.TrendingAnimeListDto

interface KitsuApi {
    companion object {
        const val baseUrl = "https://kitsu.io/api/edge/"
    }

    suspend fun getTrendingAnime(): TrendingAnimeListDto

    suspend fun getAnime(id: Int): AnimeResponseDto

    suspend fun loginCall()

}