package com.devpush.animeapp.domian.repository

import com.devpush.animeapp.domian.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val isLoginFlow: Flow<Boolean>
    val isOnboardingShownFlow: Flow<Boolean>
    val appThemeFlow: Flow<String>
    val appLanguageFlow: Flow<String>

    suspend fun updateLoginStatus(isLoggedIn: Boolean)
    suspend fun updateOnboardingShownStatus(isShown: Boolean)
    suspend fun updateAppTheme(theme: String)
    suspend fun updateAppLanguage(language: String)
    suspend fun fetchInitialPreferences(): UserPreferences
}