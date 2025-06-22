package com.devpush.animeapp.core.di.modules

import com.devpush.animeapp.features.archived.data.repository.ArchivedRepositoryImpl
import com.devpush.animeapp.features.archived.domain.repository.ArchivedRepository
import com.devpush.animeapp.features.auth.data.repository.AuthRepositoryImpl
import com.devpush.animeapp.features.auth.domain.repository.AuthRepository
import com.devpush.animeapp.features.details.data.repository.AnimeDetailsRepositoryImpl
import com.devpush.animeapp.features.details.domain.repository.AnimeDetailsRepository
import com.devpush.animeapp.features.favorited.data.repository.FavoriteRepositoryImpl
import com.devpush.animeapp.features.favorited.domain.repository.FavoriteRepository
import com.devpush.animeapp.features.trending.data.repository.TrendingAnimeRepositoryImpl
import com.devpush.animeapp.features.trending.domain.repository.TrendingAnimeRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val RepositoryModule = module {

    singleOf(::TrendingAnimeRepositoryImpl) bind TrendingAnimeRepository::class
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    singleOf(::AnimeDetailsRepositoryImpl) bind AnimeDetailsRepository::class
    singleOf(::FavoriteRepositoryImpl) bind FavoriteRepository::class
    singleOf(::ArchivedRepositoryImpl) bind ArchivedRepository::class

}