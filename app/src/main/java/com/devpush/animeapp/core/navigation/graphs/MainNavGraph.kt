package com.devpush.animeapp.core.navigation.graphs

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        HomeScreen(
            currentRoute = currentRoute,
            onNavigateToHome = {
                navController.navigate(NavRoute.Home.route) {
                    popUpTo(NavRoute.Home.route) { inclusive = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            onNavigateToTrending = {
                navController.navigate(NavRoute.Trending.route){
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            onNavigateToSettings = {
                navController.navigate(NavRoute.Settings.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            onAnimeClick = { posterImage, animeId ->
                navController.navigate(
                    NavRoute.AnimeDetail.createRoute(posterImage ?: "", animeId.toIntOrNull() ?: 0)
                )
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
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        TrendingAnimeScreen(
            currentRoute = currentRoute,
            onNavigateToHome = {
                navController.navigate(NavRoute.Home.route) {
                    popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            onNavigateToTrending = {
                navController.navigate(NavRoute.Trending.route) {
                    popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            onNavigateToSettings = {
                navController.navigate(NavRoute.Settings.route) {
                    popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            onAnimeClick = { posterImage, animeId ->
                navController.navigate(
                    NavRoute.AnimeDetail.createRoute(posterImage ?: "", animeId.toIntOrNull() ?: 0)
                )
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
