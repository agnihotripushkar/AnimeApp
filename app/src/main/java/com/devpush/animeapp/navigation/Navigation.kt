package com.devpush.animeapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.devpush.animeapp.presentation.screens.home.HomeScreen
import com.devpush.animeapp.presentation.screens.auth.LoginScreen
import com.devpush.animeapp.presentation.screens.auth.OnBoardingScreen
import com.devpush.animeapp.presentation.screens.auth.RegistrationScreen
import com.devpush.animeapp.presentation.screens.auth.WelcomeScreen
import com.devpush.animeapp.presentation.screens.trending.TrendingAnimeScreen
import com.devpush.animeapp.utils.DataStoreUtils

@Composable
fun Navigation() {
    val navHost = rememberNavController()
    val context = LocalContext.current
    val isOnboardingShown = DataStoreUtils.readOnboardingStatus(context)

    NavHost(
        navController = navHost,
        startDestination = NavGraph.Welcome.route
    ) {
        composable(NavGraph.Welcome.route) {
            WelcomeScreen(
                onOpenLoginClicked = {
                    navHost.navigate(NavGraph.Login.route)
                }
            )
        }

        composable(NavGraph.Login.route) {
            LoginScreen(onOpenRegistrationClicked = {
                navHost.navigate(NavGraph.Registration.route)
            },
                onLoginClicked = {
                    if (isOnboardingShown) {
                        navHost.navigate(NavGraph.TrendingAnime.route)
                    } else {
                        navHost.navigate(NavGraph.OnBoarding.route)
                    }
                }
            )
        }

        composable(NavGraph.Registration.route) {
            RegistrationScreen(
                onRegisterClicked = {
                    navHost.navigate(NavGraph.OnBoarding.route)
                },
                onLoginClicked = {
                    navHost.navigate(NavGraph.Login.route)
                }
            )
        }

        composable(NavGraph.Home.route) {
            HomeScreen()
        }

        composable(NavGraph.OnBoarding.route) {
            OnBoardingScreen(onGetStartedClicked = {
                navHost.navigate(NavGraph.TrendingAnime.route)
            })
        }

        composable(NavGraph.TrendingAnime.route) {
            TrendingAnimeScreen(
                onAnimeClick = { imageUrl, animeId ->
                    navHost.navigate(NavGraph.Home.route)
                }
            )
        }
    }

}
