package com.devpush.animeapp.features.trending.data.repository

import com.devpush.animeapp.core.network.ApiEndpoints
import com.devpush.animeapp.core.network.NetworkResult
import com.devpush.animeapp.features.trending.data.remote.responsebody.TrendingAnimeListResponse
import com.devpush.animeapp.data.local.dao.TrendingAnimeDao
import com.devpush.animeapp.data.local.entities.AnimeDataEntity
import com.devpush.animeapp.features.trending.domain.repository.TrendingAnimeRepository
import com.devpush.animeapp.features.trending.utils.Constants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import java.io.IOException
import java.net.UnknownHostException

class TrendingAnimeRepositoryImpl(
    val ktorClient: HttpClient,
    private val trendingAnimeDao: TrendingAnimeDao
) : TrendingAnimeRepository {
    override suspend fun getAllAnime(): NetworkResult<TrendingAnimeListResponse> {
        return try {
            val response = ktorClient.get(ApiEndpoints.ALL_ANIME_ENDPOINT).body<TrendingAnimeListResponse>()
            NetworkResult.Success(response)
        } catch (e: UnknownHostException) {
            Timber.tag(Constants.TAG)
                .e(e, "getTrendingAnime UnknownHostException: No internet or host not found")
            NetworkResult.Error(e, "Unable to resolve host. Check internet connection.")
        } catch (e: IOException) {
            Timber.tag(Constants.TAG).e(e, "getTrendingAnime IOException: Network error")
            NetworkResult.Error(e, "Network error occurred.")
        } catch (e: Exception) {
            Timber.tag(Constants.TAG)
                .e(e, "getTrendingAnime Exception: An unexpected error occurred")
            NetworkResult.Error(e, "An unexpected error occurred.")
        }
    }

    override suspend fun getTrendingAnime(): NetworkResult<TrendingAnimeListResponse> {
        return try {
            val response = ktorClient.get(ApiEndpoints.TRENDING_ANIME_ENDPOINT).body<TrendingAnimeListResponse>()
            NetworkResult.Success(response)
        } catch (e: UnknownHostException) {
            Timber.tag(Constants.TAG)
                .e(e, "getTrendingAnime UnknownHostException: No internet or host not found")
            NetworkResult.Error(e, "Unable to resolve host. Check internet connection.")
        } catch (e: IOException) {
            Timber.tag(Constants.TAG).e(e, "getTrendingAnime IOException: Network error")
            NetworkResult.Error(e, "Network error occurred.")
        } catch (e: Exception) {
            Timber.tag(Constants.TAG)
                .e(e, "getTrendingAnime Exception: An unexpected error occurred")
            NetworkResult.Error(e, "An unexpected error occurred.")
        }
    }

    override fun getAnimeFromDb(): Flow<List<AnimeDataEntity>> {
        return trendingAnimeDao.getAll()
    }

    override suspend fun saveAnime(animeList: List<AnimeDataEntity>) {
        trendingAnimeDao.deleteAll()
        trendingAnimeDao.insertAll(animeList)
    }
}