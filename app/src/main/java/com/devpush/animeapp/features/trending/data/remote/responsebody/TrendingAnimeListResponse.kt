package com.devpush.animeapp.features.trending.data.remote.responsebody

import com.devpush.animeapp.data.local.entities.AnimeAttributesDb
import com.devpush.animeapp.data.local.entities.AnimeDataEntity
import com.devpush.animeapp.data.local.entities.PosterImageDb
import kotlinx.serialization.Serializable

@Serializable
data class TrendingAnimeListResponse(
    val data: List<AnimeDataResponse>
) {
    fun toModel(): List<AnimeDataEntity> =
        data.map { it.toModel() }
}

@Serializable
data class AnimeResponseDto(
    val data: AnimeDataResponse
) {
    fun toModel(): AnimeDataEntity = data.toModel()
}

@Serializable
data class AnimeDataResponse(
    val id: String,
    val type: String,
    val attributes: AttributesDto,
) {
    fun toModel(): AnimeDataEntity =
        AnimeDataEntity(
            id = id,
            attributes = attributes.toModel()
        )
}

@Serializable
data class AttributesDto(
    val createdAt: String,
    val updatedAt: String,
    val slug: String?,
    val synopsis: String?,
    val coverImageTopOffset: Int,
    val canonicalTitle: String?,
    val abbreviatedTitles: List<String>,
    val averageRating: String?,
    val ratingFrequencies: Map<String,String>,
    val userCount: Int?,
    val favoritesCount: Int?,
    val startDate: String?,
    val endDate: String?,
    val popularityRank: Int?,
    val ratingRank: Int?,
    val ageRating: String?,
    val ageRatingGuide: String?,
    val subtype: String,
    val status: String,
    val tba: String?,
    val posterImage: PosterImageDto,
    val episodeCount: Int?,
    val episodeLength: Int?,
    val youtubeVideoId: String?,
    val nsfw: Boolean
) {
    fun toModel(): AnimeAttributesDb =
        AnimeAttributesDb(
            synopsis = synopsis,
            canonicalTitle = canonicalTitle,
            averageRating = averageRating,
            posterImage = posterImage.toModel(),
            startDate = startDate,
            endDate = endDate,
            subType = subtype,
            status = status,
            ageRating = ageRating,
            ageRatingGuide = ageRatingGuide,
            episodeCount = episodeCount,
        )
}

@Serializable
data class PosterImageDto(
    val tiny: String,
    val small: String,
    val medium: String,
    val large: String,
    val original: String,
) {
    fun toModel(): PosterImageDb =
        PosterImageDb(
            originalUrl = original
        )
}

