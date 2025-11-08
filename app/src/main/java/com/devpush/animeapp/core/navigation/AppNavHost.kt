package com.devpush.animeapp.core.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.devpush.animeapp.features.common.domain.Destination
import com.devpush.animeapp.features.home.ui.HomeScreen
import com.devpush.animeapp.features.settings.ui.SettingsScreen
import com.devpush.animeapp.features.trending.ui.TrendingAnimeScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Destination.HOME.route,
        modifier = modifier
    ) {
        composable(Destination.HOME.route) {
            HomeScreen(
                onAnimeClick = { imageUrl, animeId ->
                    val encodedUrl = Uri.encode(imageUrl)
                    navController.navigate("detail_anime/${encodedUrl}/${animeId}")
                },
                onSettingsClick = {
                    navController.navigate(NavGraph.Settings.route)
                },
                onArchiveClick = {
                    navController.navigate(NavGraph.ArchivedAnime.route)
                },
                onFavoriteClick = {
                    navController.navigate(NavGraph.FavoritedAnime.route)
                }
            )
        }
        composable(Destination.TRENDING.route) {
            TrendingAnimeScreen(
                onAnimeClick = { imageUrl, animeId ->
                    val encodedUrl = Uri.encode(imageUrl)
                    navController.navigate("detail_anime/${encodedUrl}/${animeId}")
                },
                onSettingsClick = {
                    navController.navigate(NavGraph.Settings.route)
                },
                onArchiveClick = {
                    navController.navigate(NavGraph.ArchivedAnime.route)
                },
                onFavoriteClick = {
                    navController.navigate(NavGraph.FavoritedAnime.route)
                }
            )
        }
        composable(Destination.SETTINGS.route) {
            SettingsScreen(navController = navController)
        }
    }
}