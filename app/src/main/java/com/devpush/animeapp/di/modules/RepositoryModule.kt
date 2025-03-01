package com.devpush.animeapp.di.modules

import com.devpush.animeapp.data.remote.repository.AuthRepositoryImpl
import com.devpush.animeapp.data.remote.repository.KitsuRepositoryImpl
import com.devpush.animeapp.domian.repository.AuthRepository
import com.devpush.animeapp.domian.repository.KitsuRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val RepositoryModule = module {

    singleOf(::KitsuRepositoryImpl) bind KitsuRepository::class
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class

}