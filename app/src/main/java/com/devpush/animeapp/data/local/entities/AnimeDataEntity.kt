package com.devpush.animeapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devpush.animeapp.utils.Constants

@Entity(tableName = Constants.TRENDING_ANIME_TABLE)
data class AnimeDataEntity(
    @PrimaryKey
    @ColumnInfo(name = "anime_id")
    val id: String,

    @Embedded
    val attributes: AnimeAttributesDb,

    @ColumnInfo(name = "is_favorite", defaultValue = "false")
    val isFavorite: Boolean = false,

    @ColumnInfo(name = "is_archived", defaultValue = "false")
    val isArchived: Boolean = false

    // If you want to store other fields from the original JSON that were not in AnimeData,
    // you can add them here. For example, if 'Titles' or 'CoverImage' were needed directly.
)


data class AnimeAttributesDb(
    @ColumnInfo(name = "synopsis")
    val synopsis: String?,

    @ColumnInfo(name = "canonical_title")
    val canonicalTitle: String?,

    @ColumnInfo(name = "average_rating")
    val averageRating: String?,

    @ColumnInfo(name = "start_date")
    val startDate: String?,

    @ColumnInfo(name = "endDate")
    val endDate: String?,

    @ColumnInfo(name = "subType")
    val subType: String?,

    @ColumnInfo(name = "status")
    val status: String?,

    @ColumnInfo(name = "ageRating")
    val ageRating: String?,

    @ColumnInfo(name = "ageRatingGuide")
    val ageRatingGuide: String?,

    @ColumnInfo(name = "episodeCount")
    val episodeCount: Int?,

    @Embedded(prefix = "poster_") // Prefix to avoid column name conflicts if PosterImageDb had more fields
    val posterImage: PosterImageDb
)

data class PosterImageDb(
    @ColumnInfo(name = "original_url")
    val originalUrl: String
)