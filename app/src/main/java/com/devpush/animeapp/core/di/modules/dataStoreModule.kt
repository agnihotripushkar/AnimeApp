package com.devpush.animeapp.core.di.modules

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.devpush.animeapp.data.repository.UserPreferencesRepositoryImpl
import com.devpush.animeapp.domian.repository.UserPreferencesRepository
import com.devpush.animeapp.utils.Constants
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.ANIME_PREFERENCES)

val dataStoreModule = module {
    single<DataStore<Preferences>> {
        androidContext().dataStore // Provides the application context's DataStore instance
    }

    single<UserPreferencesRepository> {
        UserPreferencesRepositoryImpl(get())
    }
}

