package com.devpush.animeapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.devpush.animeapp.MainViewModel
import com.devpush.animeapp.core.navigation.graphs.authNavGraph
import com.devpush.animeapp.core.navigation.graphs.featureNavGraph
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
        modifier = modifier,
        enterTransition = {
            slideIntoContainer(
                androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Start,
                tween(300)
            ) + fadeIn(tween(300))
        },
        exitTransition = {
            slideOutOfContainer(
                androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Start,
                tween(300)
            ) + fadeOut(tween(300))
        },
        popEnterTransition = {
            slideIntoContainer(
                androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.End,
                tween(300)
            ) + fadeIn(tween(300))
        },
        popExitTransition = {
            slideOutOfContainer(
                androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.End,
                tween(300)
            ) + fadeOut(tween(300))
        }
    ) {
        // Authentication flow: Login, Registration, BiometricAuth, OnBoarding
        authNavGraph(navController, mainViewModel)
        
        // Main flow: Home, Trending, Settings (screens with bottom navigation)
        mainNavGraph(navController)
        
        // Feature flow: AnimeDetail, Archived, Favorited
        featureNavGraph(navController)
    }
}
