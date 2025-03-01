package com.devpush.animeapp.data.remote.repository

import com.devpush.animeapp.domian.model.AnimeData
import com.devpush.animeapp.domian.repository.KitsuRepository
import com.devpush.animeapp.network.KitsuApi

class KitsuRepositoryImpl(private val api: KitsuApi): KitsuRepository {

    override suspend fun getTrendingAnime(): List<AnimeData> {
        return api.getTrendingAnime().toModel()
    }

    override suspend fun getAnime(id: Int): AnimeData? {
        return api.getAnime(id).toModel()
    }

}