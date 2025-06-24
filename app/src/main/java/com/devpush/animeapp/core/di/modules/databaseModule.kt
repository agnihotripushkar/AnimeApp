package com.devpush.animeapp.core.di.modules

import androidx.room.Room
import com.devpush.animeapp.data.local.db.AnimeDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            AnimeDatabase::class.java,
            "anime_database"
        ).build()
    }

    single {
        get<AnimeDatabase>().trendingAnimeDao()
    }
}