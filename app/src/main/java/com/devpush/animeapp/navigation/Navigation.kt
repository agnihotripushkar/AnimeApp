package com.devpush.animeapp.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.devpush.animeapp.screens.HomeScreen
import com.devpush.animeapp.screens.LoginScreen
import com.devpush.animeapp.screens.OnBoardingScreen
import com.devpush.animeapp.screens.RegistrationScreen
import com.devpush.animeapp.screens.WelcomeScreen

@Composable
fun Navigation() {
    val navHost = rememberNavController()

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
                    navHost.navigate(NavGraph.OnBoarding.route)
                }
            )
        }

        composable(NavGraph.Registration.route) {
            RegistrationScreen(
                onRegisterClicked = {
                    navHost.navigate(NavGraph.Home.route)
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
            OnBoardingScreen()
        }

    }

}
