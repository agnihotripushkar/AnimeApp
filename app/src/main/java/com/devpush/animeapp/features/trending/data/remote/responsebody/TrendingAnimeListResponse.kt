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
    val links: LinksDto,
    val attributes: AttributesDto,
    val relationships: RelationshipsDto
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
    val showType: String?,
    val nsfw: Boolean
) {
    fun toModel(): AnimeAttributesDb =
        AnimeAttributesDb(
            synopsis = synopsis,
            canonicalTitle = canonicalTitle,
            averageRating = averageRating,
            posterImage = posterImage.toModel(),
            startDate = startDate
        )
}

@Serializable
data class PosterImageDto(
    val tiny: String,
    val small: String,
    val medium: String,
    val large: String,
    val original: String,
    val meta: MetaDto?
) {
    fun toModel(): PosterImageDb =
        PosterImageDb(
            originalUrl = original
        )
}

@Serializable
data class MetaDto(
    val dimensions: DimensionsDto
)

@Serializable
data class DimensionsDto(
    val tiny: SizeDto,
    val small: SizeDto,
    val large: SizeDto
)

@Serializable
data class SizeDto(
    val width: Int? = null,
    val height: Int? = null
)


@Serializable
data class RelationshipsDto(
    val genres: RelationDto,
    val categories: RelationDto,
    val castings: RelationDto,
    val installments: RelationDto,
    val mappings: RelationDto,
    val reviews: RelationDto,
    val mediaRelationships: RelationDto,
    val episodes: RelationDto,
    val streamingLinks: RelationDto,
    val animeProductions: RelationDto,
    val animeCharacters: RelationDto,
    val animeStaff: RelationDto
)

@Serializable
data class RelationDto(
    val links: RelationLinksDto
)

@Serializable
data class LinksDto(
    val self: String
)

@Serializable
data class RelationLinksDto(
    val self: String,
    val related: String
)