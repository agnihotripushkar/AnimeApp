package com.devpush.animeapp.core.di.modules

import com.devpush.animeapp.features.archived.ui.ArchivedAnimeViewModel
import com.devpush.animeapp.features.auth.ui.AuthViewModel
import com.devpush.animeapp.features.details.ui.DetailsScreenViewModel
import com.devpush.animeapp.features.favorited.ui.FavoritedAnimeViewModel
import com.devpush.animeapp.features.main.ui.MainViewModel
import com.devpush.animeapp.features.settings.ui.SettingsViewModel
import com.devpush.animeapp.features.trending.ui.TrendingAnimeViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val presentationModule = module {
    viewModel { DetailsScreenViewModel(get()) }
    viewModel { TrendingAnimeViewModel(get()) }
    viewModel { AuthViewModel(application = get(), userPreferencesRepository = get(), authRepository = get()) }
    viewModel { MainViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { ArchivedAnimeViewModel(get()) }
    viewModel { FavoritedAnimeViewModel(get()) }
}