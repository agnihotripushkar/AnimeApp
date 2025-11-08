package com.devpush.animeapp.core.navigation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.devpush.animeapp.MainViewModel
import com.devpush.animeapp.core.navigation.graphs.authNavGraph
import com.devpush.animeapp.core.navigation.graphs.featureNavGraph
import com.devpush.animeapp.core.navigation.graphs.mainNavGraph

/**
 * Unified navigation host that contains all navigation destinations in a single NavHost.
 * 
 * This component replaces the previous nested NavHost architecture with a single,
 * unified navigation structure. It uses extension functions to organize navigation
 * graphs into logical sections (auth, main, feature).
 * 
 * Benefits:
 * - Single source of truth for navigation
 * - Simplified state management
 * - Better back stack control
 * - Follows Jetpack Compose navigation best practices
 * 
 * @param navController The navigation controller for managing navigation
 * @param mainViewModel The main view model containing app state
 * @param startDestination The initial destination route
 * @param modifier Optional modifier for the NavHost
 */
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
