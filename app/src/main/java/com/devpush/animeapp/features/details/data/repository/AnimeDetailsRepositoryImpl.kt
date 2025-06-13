package com.devpush.animeapp.features.details.data.repository

import com.devpush.animeapp.core.network.NetworkResult
import com.devpush.animeapp.features.trending.data.remote.responsebody.AnimeResponseDto
import com.devpush.animeapp.features.details.domain.repository.AnimeDetailsRepository
import com.devpush.animeapp.features.details.utils.Constants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import timber.log.Timber
import java.io.IOException
import java.net.UnknownHostException

class AnimeDetailsRepositoryImpl(val client: HttpClient) : AnimeDetailsRepository {

    override suspend fun getAnime(id: Int): NetworkResult<AnimeResponseDto> {
        return try {
            val response = client.get(Constants.ANIME.format(id)).body<AnimeResponseDto>()
            NetworkResult.Success(response)
        } catch (e: UnknownHostException) {
            Timber.tag(Constants.TAG)
                .e(e, "getAnime UnknownHostException for id $id: No internet or host not found")
            NetworkResult.Error(e, "Unable to resolve host. Check internet connection.")
        } catch (e: IOException) {
            Timber.tag(Constants.TAG).e(e, "getAnime IOException for id $id: Network error")
            NetworkResult.Error(e, "Network error occurred.")
        } catch (e: Exception) {
            Timber.tag(Constants.TAG).e(e, "getAnime Exception for id $id: An unexpected error occurred")
            NetworkResult.Error(e, "An unexpected error occurred.")
        }
    }
}