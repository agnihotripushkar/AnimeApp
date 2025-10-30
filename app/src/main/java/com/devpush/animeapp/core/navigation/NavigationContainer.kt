package com.devpush.animeapp.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.devpush.animeapp.MainViewModel
import timber.log.Timber

/**
 * NavigationContainer wraps the Navigation component and manages loading states.
 * 
 * This component ensures that navigation only renders when all authentication
 * states are fully loaded, preventing screen flashing and timing issues.
 * 
 * @param mainViewModel The main view model containing authentication states
 */
@Composable
fun NavigationContainer(
    mainViewModel: MainViewModel
) {
    val isInitialized by mainViewModel.isInitialized.collectAsState()
    val isLogin by mainViewModel.isLogin.collectAsState()
    val isBiometricEnabled by mainViewModel.isBiometricAuthEnabled.collectAsState()
    val isOnboardingShown by mainViewModel.isOnboardingShown.collectAsState()
    
    Timber.tag("NavigationContainer").d(
        "NavigationContainer state - isInitialized: $isInitialized, " +
        "isLogin: $isLogin, isBiometricEnabled: $isBiometricEnabled, " +
        "isOnboardingShown: $isOnboardingShown"
    )
    
    if (isInitialized) {
        // All states are loaded, safe to render navigation
        Timber.tag("NavigationContainer").d("All states loaded, rendering Navigation component")
        
        // Calculate start destination using the helper function
        // States are guaranteed to be non-null when isInitialized is true
        val startDestination = NavigationUtils.calculateStartDestination(
            isLogin = isLogin!!,
            isBiometricEnabled = isBiometricEnabled!!
        )
        
        Timber.tag("NavigationContainer").d("Start destination calculated: $startDestination")
        
        Navigation(
            mainViewModel = mainViewModel,
            startDestination = startDestination
        )
    } else {
        // States are still loading, show loading indicator
        Timber.tag("NavigationContainer").d("States still loading, showing loading indicator")
        
        LoadingScreen()
    }
}

/**
 * Loading screen displayed while authentication states are being loaded.
 * This prevents any content screens from being shown prematurely.
 */
@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )
    }
}