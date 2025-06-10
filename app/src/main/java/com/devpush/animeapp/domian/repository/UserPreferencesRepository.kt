package com.devpush.animeapp.domian.repository

import com.devpush.animeapp.domian.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val isLoginFlow: Flow<Boolean>
    val isOnboardingShownFlow: Flow<Boolean>

    suspend fun updateLoginStatus(isLoggedIn: Boolean)
    suspend fun updateOnboardingShownStatus(isShown: Boolean)
    suspend fun fetchInitialPreferences(): UserPreferences
}