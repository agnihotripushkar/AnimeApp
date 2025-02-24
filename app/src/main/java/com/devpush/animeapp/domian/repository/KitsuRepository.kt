package com.devpush.animeapp.domian.repository

import com.devpush.animeapp.domian.model.AnimeData

interface KitsuRepository {

    suspend fun getTrendingAnime(): List<AnimeData>

    suspend fun getAnime(id: Int): AnimeData?
}