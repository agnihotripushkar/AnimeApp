package com.devpush.animeapp.core.navigation

import android.util.Log

/**
 * Utility functions for navigation logic
 */
object NavigationUtils {
    
    private const val TAG = "NavigationUtils"
    
    /**
     * Calculates the start destination based on authentication states.
     * This is a pure function that determines which screen should be shown
     * when the app launches based on the user's authentication status.
     * 
     * @param isLogin Whether the user is currently logged in (null if not yet loaded)
     * @param isBiometricEnabled Whether biometric authentication is enabled (null if not yet loaded)
     * @return The route string for the appropriate start destination
     */
    fun calculateStartDestination(
        isLogin: Boolean?,
        isBiometricEnabled: Boolean?
    ): String {
        val destination = when {
            isLogin == true && isBiometricEnabled == true -> {
                Log.d(TAG, "User is logged in with biometric enabled -> BiometricAuth")
                NavGraph.BiometricAuth.route
            }
            isLogin == true && isBiometricEnabled == false -> {
                Log.d(TAG, "User is logged in without biometric -> TrendingAnime")
                NavGraph.TrendingAnime.route
            }
            else -> {
                Log.d(TAG, "User is not logged in (isLogin=$isLogin, isBiometricEnabled=$isBiometricEnabled) -> Login")
                NavGraph.Login.route
            }
        }
        
        Log.d(TAG, "calculateStartDestination(isLogin=$isLogin, isBiometricEnabled=$isBiometricEnabled) -> $destination")
        return destination
    }

    /**
     * Calculates the start destination based on loaded authentication states.
     * This overload is used when states are guaranteed to be loaded (non-null).
     * 
     * @param isLogin Whether the user is currently logged in
     * @param isBiometricEnabled Whether biometric authentication is enabled
     * @return The route string for the appropriate start destination
     */
    fun calculateStartDestination(
        isLogin: Boolean,
        isBiometricEnabled: Boolean
    ): String {
        val destination = when {
            isLogin && isBiometricEnabled -> {
                Log.d(TAG, "User is logged in with biometric enabled -> BiometricAuth")
                NavGraph.BiometricAuth.route
            }
            isLogin && !isBiometricEnabled -> {
                Log.d(TAG, "User is logged in without biometric -> TrendingAnime")
                NavGraph.TrendingAnime.route
            }
            else -> {
                Log.d(TAG, "User is not logged in -> Login")
                NavGraph.Login.route
            }
        }
        
        Log.d(TAG, "calculateStartDestination(isLogin=$isLogin, isBiometricEnabled=$isBiometricEnabled) -> $destination")
        return destination
    }
}