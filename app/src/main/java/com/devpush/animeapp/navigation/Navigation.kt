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
fun Navigation(){
    val navHost = rememberNavController()

    NavHost(
        navController = navHost,
        startDestination = NavGraph.Welcome.route
    ){
        composable(NavGraph.Welcome.route){
            WelcomeScreen()
        }

        composable(NavGraph.Login.route){
            LoginScreen()
        }

        composable(NavGraph.Registration.route){
            RegistrationScreen()
        }

        composable(NavGraph.Home.route){
            HomeScreen()
        }

    }

}
