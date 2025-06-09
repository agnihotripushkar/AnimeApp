package com.devpush.animeapp.features.trending.data.repository

import com.devpush.animeapp.core.network.NetworkResult
import com.devpush.animeapp.core.network.dto.TrendingAnimeListDto
import com.devpush.animeapp.features.trending.domain.repository.TrendingAnimeRepository
import com.devpush.animeapp.features.trending.utils.Constants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import timber.log.Timber
import java.io.IOException
import java.net.UnknownHostException

class TrendingAnimeRepositoryImpl(val client: HttpClient): TrendingAnimeRepository {

    override suspend fun getTrendingAnime(): NetworkResult<TrendingAnimeListDto> {
        return try {
            val response = client.get(Constants.TRENDING).body<TrendingAnimeListDto>()
            NetworkResult.Success(response)
        } catch (e: UnknownHostException) {
            Timber.tag(Constants.TAG)
                .e(e, "getTrendingAnime UnknownHostException: No internet or host not found")
            NetworkResult.Error(e, "Unable to resolve host. Check internet connection.")
        } catch (e: IOException) {
            Timber.tag(Constants.TAG).e(e, "getTrendingAnime IOException: Network error")
            NetworkResult.Error(e, "Network error occurred.")
        } catch (e: Exception) {
            Timber.tag(Constants.TAG).e(e, "getTrendingAnime Exception: An unexpected error occurred")
            NetworkResult.Error(e, "An unexpected error occurred.")
        }
    }

}