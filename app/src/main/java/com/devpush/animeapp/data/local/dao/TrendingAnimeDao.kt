package com.devpush.animeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devpush.animeapp.data.local.entities.TrendingAnimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrendingAnimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(animeList: List<TrendingAnimeEntity>)

    @Query("SELECT * FROM trending_anime")
    fun getAll(): Flow<List<TrendingAnimeEntity>>

    @Query("DELETE FROM trending_anime")
    suspend fun deleteAll()
}