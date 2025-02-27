package com.devpush.animeapp.domian.model

import kotlinx.serialization.Serializable

@Serializable
data class AnimeData(
    val id: String,
    val attributes: Attributes,
)

@Serializable
data class Attributes(
    val createdAt: String?,
    val updatedAt: String?,
    val slug: String?,
    val synopsis: String?,
    val titles: Titles,
    val canonicalTitle: String?,
    val abbreviatedTitles: List<String>,
    val averageRating: String?,
    val userCount: Int?,
    val favoritesCount: Int?,
    val startDate: String?,
    val endDate: String?,
    val popularityRank: Int?,
    val ratingRank: Int?,
    val ageRating: String?,
    val ageRatingGuide: String?,
    val subtype: String?,
    val status: String?,
    val posterImage: PosterImage,
    val coverImage: CoverImage,
    val episodeCount: Int?,
    val episodeLength: Int?,
    val showType: String?
)

@Serializable
data class Titles(
    val en: String?
)

@Serializable
data class PosterImage(
    val tiny: String,
    val small: String,
    val medium: String,
    val large: String,
    val original: String
)

@Serializable
data class CoverImage(
    val tiny: String,
    val small: String,
    val large: String,
    val original: String
)
