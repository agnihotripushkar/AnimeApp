package com.devpush.animeapp.core.navigation.graphs

import android.net.Uri
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.devpush.animeapp.core.navigation.routes.NavRoute
import com.devpush.animeapp.features.archived.ui.ArchivedAnimeScreen
import com.devpush.animeapp.features.details.ui.DetailsScreen
import com.devpush.animeapp.features.favorited.ui.FavoritedAnimeScreen

/**
 * Extension function for NavGraphBuilder that defines all feature navigation destinations.
 * This includes AnimeDetail (with route parameters), Archived, and Favorited screens.
 *
 * @param navController The NavHostController for navigation actions
 */
fun NavGraphBuilder.featureNavGraph(
    navController: NavHostController
) {
    // AnimeDetail Screen with route parameters
    composable(
        route = NavRoute.AnimeDetail.route,
        arguments = listOf(
            navArgument("coverUrl") { 
                type = NavType.StringType
                nullable = false
            },
            navArgument("id") { 
                type = NavType.IntType
                nullable = false
            }
        )
    ) { backStackEntry ->
        // Parse route arguments
        val encodedCoverUrl = backStackEntry.arguments?.getString("coverUrl") ?: ""
        val coverUrl = Uri.decode(encodedCoverUrl)
        val animeId = backStackEntry.arguments?.getInt("id") ?: 0
        
        DetailsScreen(
            id = animeId,
            coverImage = coverUrl,
            onNavigateBack = {
                navController.navigateUp()
            }
        )
    }
    
    // Archived Screen
    composable(NavRoute.Archived.route) {
        ArchivedAnimeScreen(
            onNavigateBack = {
                navController.navigateUp()
            },
            onAnimeClick = { animeId ->
                // Navigate to detail screen when anime card is clicked
                // Note: We need the cover URL, but it's not available from the click
                // The screen will need to be updated to pass the cover URL
                // For now, we'll pass an empty string and let the detail screen handle it
                navController.navigate(
                    NavRoute.AnimeDetail.createRoute("", animeId.toIntOrNull() ?: 0)
                )
            }
        )
    }
    
    // Favorited Screen
    composable(NavRoute.Favorited.route) {
        FavoritedAnimeScreen(
            onNavigateBack = {
                navController.navigateUp()
            },
            onAnimeClick = { animeId ->
                // Navigate to detail screen when anime card is clicked
                // Note: We need the cover URL, but it's not available from the click
                // The screen will need to be updated to pass the cover URL
                // For now, we'll pass an empty string and let the detail screen handle it
                navController.navigate(
                    NavRoute.AnimeDetail.createRoute("", animeId.toIntOrNull() ?: 0)
                )
            }
        )
    }
}
