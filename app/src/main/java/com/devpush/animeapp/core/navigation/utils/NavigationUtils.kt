package com.devpush.animeapp.core.navigation.utils

import android.util.Log
import com.devpush.animeapp.core.navigation.routes.NavRoute
import timber.log.Timber

object NavigationUtils {
    
    private const val TAG = "NavigationUtils"

    fun calculateStartDestination(
        isLogin: Boolean,
        isBiometricEnabled: Boolean
    ): String {
        val destination = when {
            isLogin && isBiometricEnabled -> {
                Timber.tag(TAG).d("User is logged in with biometric enabled -> BiometricAuth")
                NavRoute.BiometricAuth.route
            }
            isLogin && !isBiometricEnabled -> {
                Timber.tag(TAG).d("User is logged in without biometric -> Home")
                NavRoute.Home.route
            }
            else -> {
                Timber.tag(TAG).d("User is not logged in -> Login")
                NavRoute.Login.route
            }
        }

        Timber.tag(TAG)
            .d("calculateStartDestination(isLogin=$isLogin, isBiometricEnabled=$isBiometricEnabled) -> $destination")
        return destination
    }
}
