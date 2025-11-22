package com.devpush.animeapp.core.navigation.routes

import android.net.Uri

/**
 * Centralized, type-safe route definitions for the entire navigation system.
 * Replaces the previous NavGraph and Destination enum pattern.
 */
sealed class NavRoute(val route: String) {
    
    // Auth Routes
    data object Login : NavRoute("login")
    data object Registration : NavRoute("registration")
    data object BiometricAuth : NavRoute("biometric_auth")
    data object OnBoarding : NavRoute("onboarding")
    
    // Main Routes (with bottom navigation)
    data object Home : NavRoute("home")
    data object Trending : NavRoute("trending")
    data object Settings : NavRoute("settings")
    
    // Feature Routes
    data object Archived : NavRoute("archived")
    data object Favorited : NavRoute("favorited")
    
    // Parameterized Routes
    data object AnimeDetail : NavRoute("anime_detail/{coverUrl}/{id}") {
        /**
         * Creates a route with the provided parameters for navigation.
         * @param coverUrl The cover image URL (will be URI encoded)
         * @param id The anime ID
         * @return The formatted route string
         */
        fun createRoute(coverUrl: String, id: Int): String {
            val encodedUrl = Uri.encode(coverUrl)
            return "anime_detail/$encodedUrl/$id"
        }
    }
    
    companion object {
        /**
         * Routes that should display the bottom navigation bar.
         */
        val bottomNavRoutes by lazy {
            setOf(
                Home.route,
                Trending.route,
                Settings.route
            )
        }
        
        /**
         * Routes that should display the top app bar.
         */
        val topBarRoutes by lazy {
            setOf(
                Home.route,
                Trending.route,
                Settings.route,
                Archived.route,
                Favorited.route
            )
        }
    }
}
