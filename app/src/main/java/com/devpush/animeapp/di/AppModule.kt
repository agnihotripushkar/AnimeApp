package com.devpush.animeapp.di

import com.devpush.animeapp.di.modules.dataModule
import com.devpush.animeapp.di.modules.domainModule
import com.devpush.animeapp.di.modules.presentationModule
import org.koin.dsl.module

val appModule = module {
    // Add application-wide dependencies here
    includes(dataModule, domainModule, presentationModule)
}