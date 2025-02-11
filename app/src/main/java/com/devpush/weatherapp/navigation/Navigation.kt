package com.devpush.weatherapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devpush.weatherapp.screens.HomeScreen
import com.devpush.weatherapp.screens.LoginScreen
import com.devpush.weatherapp.screens.RegistrationScreen
import com.devpush.weatherapp.screens.WelcomeScreen

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
