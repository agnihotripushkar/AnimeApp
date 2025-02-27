package com.devpush.animeapp.network

import com.devpush.animeapp.network.dto.AnimeResponseDto
import com.devpush.animeapp.network.dto.TrendingAnimeListDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class KitsuApiImpl(val client: HttpClient) : KitsuApi {

    companion object {
        const val trending = "trending/anime"
        const val anime = "anime/%s"
    }

    override suspend fun getTrendingAnime(): TrendingAnimeListDto =
        client.get(trending).body<TrendingAnimeListDto>()


    override suspend fun getAnime(id: Int): AnimeResponseDto {
        return client.get(anime.format(id)).body<AnimeResponseDto>()
    }


}