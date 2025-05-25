package com.devpush.animeapp.domian.repository

import com.devpush.animeapp.domian.model.AnimeData
import com.devpush.animeapp.network.NetworkResult

interface KitsuRepository {

    suspend fun getTrendingAnime(): NetworkResult<List<AnimeData>>

    suspend fun getAnime(id: Int): NetworkResult<AnimeData?>
}