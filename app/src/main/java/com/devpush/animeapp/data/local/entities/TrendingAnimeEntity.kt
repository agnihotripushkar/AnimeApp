package com.devpush.animeapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trending_anime")
data class TrendingAnimeEntity(
    @PrimaryKey val id: String,
    val title: String,
    val imageUrl: String,
    val genres: List<String>,
    val score: Double,
    val type: String,
    val format: String
)