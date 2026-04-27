package com.devpush.animeapp.core.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.MaterialTheme
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
    val motionScheme = MaterialTheme.motionScheme
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = {
            slideIntoContainer(
                androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Start,
                motionScheme.defaultSpatialSpec()
            ) + fadeIn(motionScheme.defaultEffectsSpec())
        },
        exitTransition = {
            slideOutOfContainer(
                androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Start,
                motionScheme.defaultEffectsSpec()
            ) + fadeOut(motionScheme.defaultEffectsSpec())
        },
        popEnterTransition = {
            slideIntoContainer(
                androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.End,
                motionScheme.defaultSpatialSpec()
            ) + fadeIn(motionScheme.defaultEffectsSpec())
        },
        popExitTransition = {
            slideOutOfContainer(
                androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.End,
                motionScheme.defaultEffectsSpec()
            ) + fadeOut(motionScheme.defaultEffectsSpec())
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
