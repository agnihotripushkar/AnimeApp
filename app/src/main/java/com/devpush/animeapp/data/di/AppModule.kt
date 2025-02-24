package com.devpush.animeapp.data.di

import com.devpush.animeapp.data.di.modules.dataModule
import com.devpush.animeapp.data.di.modules.domainModule
import com.devpush.animeapp.data.di.modules.presentationModule
import org.koin.dsl.module

val appModule = module {
    // Add application-wide dependencies here
    includes(dataModule, domainModule, presentationModule)
}