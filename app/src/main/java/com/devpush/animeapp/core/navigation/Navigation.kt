package com.devpush.animeapp.core.navigation

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devpush.animeapp.MainViewModel
import com.devpush.animeapp.features.archived.ui.ArchivedAnimeScreen
import com.devpush.animeapp.features.auth.ui.biometric.BiometricAuthScreen
import com.devpush.animeapp.features.auth.ui.login.LoginScreen
import com.devpush.animeapp.features.auth.ui.signup.RegistrationScreen
import com.devpush.animeapp.features.common.ui.BottomNavbar
import com.devpush.animeapp.features.details.ui.DetailsScreen
import com.devpush.animeapp.features.favorited.ui.FavoritedAnimeScreen
import com.devpush.animeapp.features.onBoarding.ui.OnBoardingScreen

@Composable
fun Navigation(
    mainViewModel: MainViewModel,
    startDestination: String? = null
) {
    val navHostController = rememberNavController()
    val isOnboardingShown = mainViewModel.isOnboardingShown.collectAsState().value

    val finalStartDestination = startDestination ?: run {
        val isLogin = mainViewModel.isLogin.collectAsState().value
        val isBiometricEnabled = mainViewModel.isBiometricAuthEnabled.collectAsState().value
        NavigationUtils.calculateStartDestination(isLogin, isBiometricEnabled)
    }

    NavHost(
        navController = navHostController,
        startDestination = finalStartDestination
    ) {
        composable(NavGraph.Login.route) {
            LoginScreen(
                onOpenRegistrationClicked = {
                    navHostController.navigate(NavGraph.Registration.route)
                },
                onLoginSuccessNavigation = {
                    if (isOnboardingShown == true) {
                        navHostController.navigate(NavGraph.HomeAnime.route) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    } else {
                        navHostController.navigate(NavGraph.OnBoarding.route) {
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
                    navHostController.navigate(NavGraph.OnBoarding.route) {
                        popUpTo(NavGraph.Registration.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onLoginClicked = {
                    navHostController.navigate(NavGraph.Login.route) {
                        popUpTo(NavGraph.Registration.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(NavGraph.OnBoarding.route) {
            OnBoardingScreen(onGetStartedClicked = {
                navHostController.navigate(NavGraph.HomeAnime.route) {
                    popUpTo(0) { inclusive = true }
                }
                mainViewModel.saveIsOnboardingShown(true)
            })
        }

        composable(NavGraph.BiometricAuth.route) {
            BiometricAuthScreen(
                onAuthSuccess = {
                    navHostController.navigate(NavGraph.HomeAnime.route) {
                        popUpTo(NavGraph.BiometricAuth.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onAuthCancelledOrFailed = { // E.g., user cancels, or too many failed attempts
                    navHostController.navigate(NavGraph.Login.route) { popUpTo(0) { inclusive = true } }
                }
            )
        }

        composable(NavGraph.HomeAnime.route) {
            MainScreen()
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

        composable(route = NavGraph.ArchivedAnime.route) {
            ArchivedAnimeScreen(navController = navHostController)
        }
        composable(route = NavGraph.FavoritedAnime.route) {
            FavoritedAnimeScreen(navController = navHostController)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val mainNavController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavbar(navController = mainNavController) }
    ) {
        AppNavHost(navController = mainNavController, modifier = Modifier.padding(0.dp))
    }
}