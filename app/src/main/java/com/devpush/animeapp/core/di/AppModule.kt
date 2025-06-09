package com.devpush.animeapp.core.di

import com.devpush.animeapp.core.di.modules.RepositoryModule
import com.devpush.animeapp.core.di.modules.networkKoinModule
import com.devpush.animeapp.core.di.modules.presentationModule
import org.koin.dsl.module

val appModule = module {
    // Add application-wide dependencies here
    includes(networkKoinModule, RepositoryModule, presentationModule)
}