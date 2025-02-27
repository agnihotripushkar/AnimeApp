package com.devpush.animeapp

import android.app.Application
import com.devpush.animeapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Log Koin into Android logger
            androidLogger()
            androidContext(this@MyApplication)
            modules(appModule)
        }

    }
}