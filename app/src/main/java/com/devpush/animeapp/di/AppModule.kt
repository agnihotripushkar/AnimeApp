package com.devpush.animeapp.di

import com.devpush.animeapp.di.modules.RepositoryModule
import com.devpush.animeapp.di.modules.networkKoinModule
import com.devpush.animeapp.di.modules.presentationModule
import org.koin.dsl.module

val appModule = module {
    // Add application-wide dependencies here
    includes(networkKoinModule, RepositoryModule, presentationModule)
}