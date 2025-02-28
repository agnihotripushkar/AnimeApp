package com.devpush.animeapp.di.modules

import com.devpush.animeapp.presentation.screens.details.DetailsScreenViewModel
import com.devpush.animeapp.presentation.screens.trending.TrendingAnimeViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val presentationModule = module {

    viewModel { DetailsScreenViewModel(get()) }
    viewModel { TrendingAnimeViewModel(get()) }

}