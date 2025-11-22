package com.devpush.animeapp.core.navigation.graphs

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.devpush.animeapp.MainViewModel
import com.devpush.animeapp.core.navigation.routes.NavRoute
import com.devpush.animeapp.features.auth.ui.biometric.BiometricAuthScreen
import com.devpush.animeapp.features.auth.ui.login.LoginScreen
import com.devpush.animeapp.features.auth.ui.signup.RegistrationScreen
import com.devpush.animeapp.features.onBoarding.ui.OnBoardingScreen

/**
 * Extension function for NavGraphBuilder that defines all authentication-related navigation destinations.
 * This includes Login, Registration, BiometricAuth, and OnBoarding screens.
 *
 * @param navController The NavHostController for navigation actions
 * @param mainViewModel The MainViewModel for accessing app-wide state (onboarding status, etc.)
 */
fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    // Login Screen
    composable(NavRoute.Login.route) {
        val isOnboardingShown by mainViewModel.isOnboardingShown.collectAsState()
        
        LoginScreen(
            onOpenRegistrationClicked = {
                navController.navigate(NavRoute.Registration.route)
            },
            onLoginSuccessNavigation = {
                val destination = if (isOnboardingShown == true) {
                    NavRoute.Home.route
                } else {
                    NavRoute.OnBoarding.route
                }
                
                // Clear the auth back stack and navigate to the destination
                navController.navigate(destination) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
    }
    
    // Registration Screen
    composable(NavRoute.Registration.route) {
        val isOnboardingShown by mainViewModel.isOnboardingShown.collectAsState()
        
        RegistrationScreen(
            onRegisterSuccessNavigation = {
                val destination = if (isOnboardingShown == true) {
                    NavRoute.Home.route
                } else {
                    NavRoute.OnBoarding.route
                }
                
                // Clear the auth back stack and navigate to the destination
                navController.navigate(destination) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            },
            onLoginClicked = {
                navController.navigate(NavRoute.Login.route) {
                    popUpTo(NavRoute.Login.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
    }
    
    // Biometric Authentication Screen
    composable(NavRoute.BiometricAuth.route) {
        val isOnboardingShown by mainViewModel.isOnboardingShown.collectAsState()
        
        BiometricAuthScreen(
            onAuthSuccess = {
                val destination = if (isOnboardingShown == true) {
                    NavRoute.Home.route
                } else {
                    NavRoute.OnBoarding.route
                }
                
                // Clear the auth back stack and navigate to the destination
                navController.navigate(destination) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            },
            onAuthCancelledOrFailed = {
                // Navigate to login screen if biometric auth fails or is cancelled
                navController.navigate(NavRoute.Login.route) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
    }
    
    // OnBoarding Screen
    composable(NavRoute.OnBoarding.route) {
        OnBoardingScreen(
            onGetStartedClicked = {
                // Mark onboarding as shown
                mainViewModel.saveIsOnboardingShown(true)
                
                // Navigate to home and clear the back stack
                navController.navigate(NavRoute.Home.route) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
    }
}
