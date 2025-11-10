package com.devpush.animeapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.devpush.animeapp.MainViewModel
import com.devpush.animeapp.core.navigation.graphs.authNavGraph
import com.devpush.animeapp.core.navigation.graphs.featureNavGraph
import com.devpush.animeapp.core.navigation.graphs.mainNavGraph

@Composable
fun UnifiedNavHost(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Authentication flow: Login, Registration, BiometricAuth, OnBoarding
        authNavGraph(navController, mainViewModel)
        
        // Main flow: Home, Trending, Settings (screens with bottom navigation)
        mainNavGraph(navController)
        
        // Feature flow: AnimeDetail, Archived, Favorited
        featureNavGraph(navController)
    }
}
