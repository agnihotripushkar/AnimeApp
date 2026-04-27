package com.devpush.animeapp.core.di.modules

import com.devpush.animeapp.features.archived.ui.ArchivedAnimeViewModel
import com.devpush.animeapp.features.auth.ui.biometric.BiometricAuthViewModel
import com.devpush.animeapp.features.auth.ui.login.LoginViewModel
import com.devpush.animeapp.features.auth.ui.signup.RegistrationViewModel
import com.devpush.animeapp.features.details.ui.DetailsScreenViewModel
import com.devpush.animeapp.features.favorited.ui.FavoritedAnimeViewModel
import com.devpush.animeapp.MainViewModel
import com.devpush.animeapp.features.home.ui.HomeViewModel
import com.devpush.animeapp.features.settings.ui.SettingsViewModel
import com.devpush.animeapp.features.trending.ui.TrendingAnimeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::DetailsScreenViewModel)
    viewModelOf(::TrendingAnimeViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::BiometricAuthViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::ArchivedAnimeViewModel)
    viewModelOf(::FavoritedAnimeViewModel)
}