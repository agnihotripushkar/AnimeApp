package com.devpush.animeapp.core.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.devpush.animeapp.core.navigation.routes.NavRoute
import com.devpush.animeapp.features.home.ui.HomeScreen
import com.devpush.animeapp.features.settings.ui.SettingsScreen
import com.devpush.animeapp.features.trending.ui.TrendingAnimeScreen

/**
 * Extension function for NavGraphBuilder that defines all main navigation destinations.
 * This includes Home, Trending, and Settings screens - the primary screens with bottom navigation.
 *
 * @param navController The NavHostController for navigation actions
 */
fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController
) {
    // Home Screen
    composable(NavRoute.Home.route) {
        HomeScreen(
            navController = navController,
            onAnimeClick = { posterImage, animeId ->
                navController.navigate(
                    NavRoute.AnimeDetail.createRoute(posterImage ?: "", animeId.toIntOrNull() ?: 0)
                )
            },
            onSettingsClick = {
                navController.navigate(NavRoute.Settings.route)
            },
            onArchiveClick = {
                navController.navigate(NavRoute.Archived.route)
            },
            onFavoriteClick = {
                navController.navigate(NavRoute.Favorited.route)
            }
        )
    }
    
    // Trending Screen
    composable(NavRoute.Trending.route) {
        TrendingAnimeScreen(
            onAnimeClick = { posterImage, animeId ->
                navController.navigate(
                    NavRoute.AnimeDetail.createRoute(posterImage ?: "", animeId.toIntOrNull() ?: 0)
                )
            },
            onSettingsClick = {
                navController.navigate(NavRoute.Settings.route)
            },
            onArchiveClick = {
                navController.navigate(NavRoute.Archived.route)
            },
            onFavoriteClick = {
                navController.navigate(NavRoute.Favorited.route)
            }
        )
    }
    
    // Settings Screen
    composable(NavRoute.Settings.route) {
        SettingsScreen(
            onNavigateBack = {
                navController.navigateUp()
            },
            onLogout = {
                navController.navigate(NavRoute.Login.route) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
    }
}
