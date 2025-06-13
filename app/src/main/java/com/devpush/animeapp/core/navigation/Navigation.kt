package com.devpush.animeapp.core.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.devpush.animeapp.features.main.ui.MainViewModel
import com.devpush.animeapp.features.auth.ui.login.LoginScreen
import com.devpush.animeapp.features.onBoarding.ui.OnBoardingScreen
import com.devpush.animeapp.features.auth.ui.signup.RegistrationScreen
import com.devpush.animeapp.features.details.ui.DetailsScreen
import com.devpush.animeapp.features.trending.ui.TrendingAnimeScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.devpush.animeapp.features.archived.ui.ArchivedAnimeScreen
import com.devpush.animeapp.features.bookmarked.ui.BookmarkedAnimeScreen
import com.devpush.animeapp.features.favorited.ui.FavoritedAnimeScreen
import com.devpush.animeapp.features.settings.ui.SettingsScreen

@Composable
fun Navigation(
    mainViewModel: MainViewModel
) {
    val navHost = rememberNavController()

    val isLogin = mainViewModel.isLogin.collectAsState().value
    val isOnboardingShown = mainViewModel.isOnboardingShown.collectAsState().value

    val startDestination = remember(isLogin) {
        when {
            isLogin == true -> NavGraph.TrendingAnime.route
            else -> NavGraph.Login.route
        }
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
                onLoginSuccessNavigation = {
                    if (isOnboardingShown == true) {
                        navHost.navigate(NavGraph.TrendingAnime.route) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    } else {
                        navHost.navigate(NavGraph.OnBoarding.route) {
                            popUpTo(NavGraph.Login.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable(NavGraph.Registration.route) {
            RegistrationScreen(
                onRegisterSuccessNavigation = {
                    navHost.navigate(NavGraph.OnBoarding.route) {
                        popUpTo(NavGraph.Registration.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onLoginClicked = {
                    navHost.navigate(NavGraph.Login.route){
                        popUpTo(NavGraph.Registration.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(NavGraph.OnBoarding.route) {
            OnBoardingScreen(onGetStartedClicked = {
                navHost.navigate(NavGraph.TrendingAnime.route) {
                    popUpTo(0) { inclusive = true }
                }
                mainViewModel.saveIsOnboardingShown(true)
            })
        }

        composable(NavGraph.TrendingAnime.route) {
            TrendingAnimeScreen(
                onAnimeClick = { imageUrl, animeId ->
                    val encodedUrl = Uri.encode(imageUrl)
                    navHost.navigate("detail_anime/${encodedUrl}/${animeId}")
                },
                onSettingsClick = {
                    navHost.navigate(NavGraph.Settings.route)
                },
                onArchiveClick = {
                    navHost.navigate(NavGraph.ArchivedAnime.route)
                },
                onFavoriteClick = {
                    navHost.navigate(NavGraph.FavoritedAnime.route)
                },
                onBookmarkClick = {
                    navHost.navigate(NavGraph.BookmarkedAnime.route)
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

        composable(NavGraph.Settings.route) {
            SettingsScreen(navController = navHost,)
        }

        composable(route = NavGraph.ArchivedAnime.route) {
            ArchivedAnimeScreen(navController = navHost)
        }
        composable(route = NavGraph.FavoritedAnime.route) {
            FavoritedAnimeScreen(navController = navHost)
        }
        composable(route = NavGraph.BookmarkedAnime.route) {
            BookmarkedAnimeScreen(navController = navHost)
        }
    }
}
