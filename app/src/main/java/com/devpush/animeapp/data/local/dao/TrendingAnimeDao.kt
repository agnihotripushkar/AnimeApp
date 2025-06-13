package com.devpush.animeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devpush.animeapp.data.local.entities.AnimeDataEntity
import com.devpush.animeapp.utils.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface TrendingAnimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(animeList: List<AnimeDataEntity>)

    @Query("SELECT * FROM ${Constants.TRENDING_ANIME_TABLE}")
    fun getAll(): Flow<List<AnimeDataEntity>>

    @Query("DELETE FROM ${Constants.TRENDING_ANIME_TABLE}")
    suspend fun deleteAll()
}