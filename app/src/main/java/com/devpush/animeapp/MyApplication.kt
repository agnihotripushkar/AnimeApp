package com.devpush.animeapp

import android.app.Application
import com.devpush.animeapp.core.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            // Log Koin into Android logger
            androidLogger()
            androidContext(this@MyApplication)
            modules(appModule)
        }

    }
}