package com.devpush.animeapp.core.navigation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.devpush.animeapp.MainViewModel
import com.devpush.animeapp.core.navigation.routes.NavRoute
import com.devpush.animeapp.features.common.ui.BottomNavbar
import timber.log.Timber

/**
 * Smart scaffold component that conditionally displays UI elements based on the current route.
 * 
 * This component wraps the navigation host and provides:
 * - Conditional bottom navigation bar (shown only on Home, Trending, Settings)
 * - Proper padding values for content
 * - Centralized UI element visibility logic
 * 
 * @param navController The navigation controller for managing navigation
 * @param mainViewModel The main view model containing app state
 * @param startDestination The initial destination route
 * @param modifier Optional modifier for the scaffold
 * @param content The content composable (typically UnifiedNavHost)
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AnimeAppScaffold(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    startDestination: String,
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit
) {
    // Observe the current back stack entry to determine the current route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    // Determine if bottom navigation should be shown based on current route
    val shouldShowBottomNav = currentRoute in NavRoute.bottomNavRoutes
    
    Timber.tag("AnimeAppScaffold").d(
        "Current route: $currentRoute, shouldShowBottomNav: $shouldShowBottomNav"
    )
    
    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (shouldShowBottomNav) {
                BottomNavbar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        Timber.tag("AnimeAppScaffold").d("Bottom nav navigation to: $route")
                        navController.navigate(route) {
                            // Pop up to the start destination to avoid building up a large back stack
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination
                            launchSingleTop = true
                            // Restore state when navigating back to a previously visited destination
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        // Only apply bottom padding to avoid overlap with bottom navigation
        // Don't apply top/start/end padding to reduce excessive spacing
        content(modifier)
    }
}
