package com.devpush.animeapp.network

import android.util.Log
import com.devpush.animeapp.network.dto.AnimeResponseDto
import com.devpush.animeapp.network.dto.TrendingAnimeListDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import java.io.IOException
import java.net.UnknownHostException

class KitsuApiImpl(val client: HttpClient) : KitsuApi {

    companion object {
        const val TAG = "KitsuApi"
        const val trending = "trending/anime"
        const val anime = "anime/%s"
    }

    override suspend fun getTrendingAnime(): NetworkResult<TrendingAnimeListDto> {
        return try {
            val response = client.get(trending).body<TrendingAnimeListDto>()
            NetworkResult.Success(response)
        } catch (e: UnknownHostException) {
            Log.e(TAG, "getTrendingAnime UnknownHostException: No internet or host not found", e)
            NetworkResult.Error(e, "Unable to resolve host. Check internet connection.")
        } catch (e: IOException) {
            Log.e(TAG, "getTrendingAnime IOException: Network error", e)
            NetworkResult.Error(e, "Network error occurred.")
        } catch (e: Exception) {
            Log.e(TAG, "getTrendingAnime Exception: An unexpected error occurred", e)
            NetworkResult.Error(e, "An unexpected error occurred.")
        }
}

    override suspend fun getAnime(id: Int): NetworkResult<AnimeResponseDto> {
        return try {
            val response = client.get(anime.format(id)).body<AnimeResponseDto>()
            NetworkResult.Success(response)
        } catch (e: UnknownHostException) {
            Log.e(TAG, "getAnime UnknownHostException for id $id: No internet or host not found", e)
            NetworkResult.Error(e, "Unable to resolve host. Check internet connection.")
        } catch (e: IOException) {
            Log.e(TAG, "getAnime IOException for id $id: Network error", e)
            NetworkResult.Error(e, "Network error occurred.")
        } catch (e: Exception) {
            Log.e(TAG, "getAnime Exception for id $id: An unexpected error occurred", e)
            NetworkResult.Error(e, "An unexpected error occurred.")
        }
    }

    override suspend fun loginCall() {

    }


}