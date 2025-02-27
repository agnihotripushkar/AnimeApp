package com.devpush.animeapp.di.modules

import com.devpush.animeapp.data.repository.KitsuRepositoryImpl
import com.devpush.animeapp.domian.repository.KitsuRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val RepositoryModule = module {

    singleOf(::KitsuRepositoryImpl) bind KitsuRepository::class

}