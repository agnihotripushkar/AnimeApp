package com.devpush.animeapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.devpush.animeapp.screens.HomeScreen
import com.devpush.animeapp.screens.LoginScreen
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
                    navHost.navigate(NavGraph.Home.route)
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

    }

}
