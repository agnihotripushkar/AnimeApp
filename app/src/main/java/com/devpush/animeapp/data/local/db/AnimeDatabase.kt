package com.devpush.animeapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.devpush.animeapp.data.local.dao.TrendingAnimeDao
import com.devpush.animeapp.data.local.entities.TrendingAnimeEntity

@Database(entities = [TrendingAnimeEntity::class], version = 1, exportSchema = false)
@TypeConverters(DBConverter::class)
abstract class AnimeDatabase : RoomDatabase() {
    abstract fun trendingAnimeDao(): TrendingAnimeDao
}