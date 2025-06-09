package com.devpush.animeapp.core.di.modules

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.devpush.animeapp.features.auth.ui.AuthViewModel
import com.devpush.animeapp.features.details.ui.DetailsScreenViewModel
import com.devpush.animeapp.features.trending.ui.TrendingAnimeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

private val Context.dataStore by preferencesDataStore("settings")

val presentationModule = module {
    single<DataStore<Preferences>> { androidContext().dataStore } // Provide DataStore
    viewModel { DetailsScreenViewModel(get()) }
    viewModel { TrendingAnimeViewModel(get()) }
    viewModel { AuthViewModel(get(), get()) }

}