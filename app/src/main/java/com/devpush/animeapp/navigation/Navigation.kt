package com.devpush.animeapp.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.devpush.animeapp.AuthViewModel
import com.devpush.animeapp.presentation.screens.auth.LoginScreen
import com.devpush.animeapp.presentation.screens.auth.OnBoardingScreen
import com.devpush.animeapp.presentation.screens.auth.RegistrationScreen
import com.devpush.animeapp.presentation.screens.details.DetailsScreen
import com.devpush.animeapp.presentation.screens.trending.TrendingAnimeScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun Navigation(
    authViewModel: AuthViewModel
) {
    val navHost = rememberNavController()
    val context = LocalContext.current // Not strictly needed here unless you're writing to DataStore directly

    val isLogin by authViewModel.isLogin.collectAsState()
    val isOnboardingShown by authViewModel.isOnboardingShown.collectAsState()
    val isInitialized by authViewModel.isInitialized.collectAsState()

    // Show nothing until preferences are loaded
    if (!isInitialized) return

    val startDestination = when {
        isLogin == true && isOnboardingShown == false -> NavGraph.OnBoarding.route
        isLogin == true -> NavGraph.TrendingAnime.route
        else -> NavGraph.Login.route
    }


    NavHost(
        navController = navHost,
        startDestination = startDestination
    ) {
        composable(NavGraph.Login.route) {
            LoginScreen(
                onOpenRegistrationClicked = {
                    navHost.navigate(NavGraph.Registration.route)
                },
                onLoginClicked = {
                    if (isOnboardingShown == true) {
                        navHost.navigate(NavGraph.TrendingAnime.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    } else {
                        navHost.navigate(NavGraph.OnBoarding.route) {
                            popUpTo(NavGraph.Login.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(NavGraph.Registration.route) {
            RegistrationScreen(
                onRegisterClicked = {
                    navHost.navigate(NavGraph.OnBoarding.route) {
                        popUpTo(NavGraph.Registration.route) { inclusive = true }
                    }
                },
                onLoginClicked = {
                    navHost.navigate(NavGraph.Login.route)
                }
            )
        }

        composable(NavGraph.OnBoarding.route) {
            OnBoardingScreen(onGetStartedClicked = {
                navHost.navigate(NavGraph.TrendingAnime.route) {
                    popUpTo(0) { inclusive = true }
                }
            })
        }

        composable(NavGraph.TrendingAnime.route) {
            TrendingAnimeScreen(
                onAnimeClick = { imageUrl, animeId ->
                    val encodedUrl = Uri.encode(imageUrl)
                    navHost.navigate("detail_anime/${encodedUrl}/${animeId}")
                }
            )
        }

        composable(
            route = NavGraph.DetailAnime.route,
            arguments = listOf(
                navArgument("coverurl") { type = NavType.StringType },
                navArgument("id") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString("coverurl") ?: ""
            val decodedUrl = Uri.decode(encodedUrl)
            val id = backStackEntry.arguments?.getString("id") ?: ""
            DetailsScreen(
                id = id.toInt(),
                coverImage = decodedUrl,
            )
        }
    }
}
