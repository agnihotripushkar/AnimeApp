package com.devpush.animeapp.data.remote.repository

import android.util.Log
import com.devpush.animeapp.domian.model.AnimeData
import com.devpush.animeapp.domian.repository.KitsuRepository
import com.devpush.animeapp.network.KitsuApi
import com.devpush.animeapp.network.NetworkResult

class KitsuRepositoryImpl(private val api: KitsuApi): KitsuRepository {
    private val TAG = "KitsuRepositoryImpl"

    override suspend fun getTrendingAnime(): NetworkResult<List<AnimeData>> {
        return when (val apiResult = api.getTrendingAnime()) {
            is NetworkResult.Success -> {
                // Successfully fetched DTO, now map it to domain model
                val animeDataList = apiResult.data.toModel()
                NetworkResult.Success(animeDataList)
            }
            is NetworkResult.Error -> {
                // Pass the error through
                Log.e(TAG, "getTrendingAnime failed: ${apiResult.message}", apiResult.exception)
                NetworkResult.Error(apiResult.exception, apiResult.message)
            }
            is NetworkResult.Loading -> {
                // Pass loading state through
                NetworkResult.Loading
            }
        }
    }

    override suspend fun getAnime(id: Int): NetworkResult<AnimeData?> {
        return when (val apiResult = api.getAnime(id)) {
            is NetworkResult.Success -> {
                // Successfully fetched DTO, now map it to domain model
                val animeData = apiResult.data.toModel()
                NetworkResult.Success(animeData) // animeData can be null if toModel() returns null
            }
            is NetworkResult.Error -> {
                // Pass the error through
                Log.e(TAG, "getAnime for id $id failed: ${apiResult.message}", apiResult.exception)
                NetworkResult.Error(apiResult.exception, apiResult.message)
            }
            is NetworkResult.Loading -> {
                // Pass loading state through
                NetworkResult.Loading
            }
        }
    }

}