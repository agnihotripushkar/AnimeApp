package com.devpush.animeapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.devpush.animeapp.domian.model.UserPreferences
import com.devpush.animeapp.domian.repository.UserPreferencesRepository
import com.devpush.animeapp.utils.PreferenceKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {

    override val isLoginFlow: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[PreferenceKeys.IS_LOGIN] ?: false
        }

    override val isOnboardingShownFlow: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[PreferenceKeys.IS_ONBOARDING_SHOWN] ?: false
        }

    override val appThemeFlow: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[PreferenceKeys.APP_THEME] ?: "system"
        }

    override val appLanguageFlow: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[PreferenceKeys.APP_LANGUAGE] ?: "en"
        }

    override suspend fun updateLoginStatus(isLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.IS_LOGIN] = isLoggedIn
        }
    }

    override suspend fun updateOnboardingShownStatus(isShown: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.IS_ONBOARDING_SHOWN] = isShown
        }
    }

    override suspend fun updateAppTheme(theme: String) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.APP_THEME] = theme
        }
    }

    override suspend fun updateAppLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.APP_LANGUAGE] = language
        }
    }

    override suspend fun fetchInitialPreferences(): UserPreferences {
        val preferences = dataStore.data.first()
        return UserPreferences(
            isLogin = preferences[PreferenceKeys.IS_LOGIN] ?: false,
            isOnboardingShown = preferences[PreferenceKeys.IS_ONBOARDING_SHOWN] ?: false,
            selectedTheme = preferences[PreferenceKeys.APP_THEME] ?: "system",
            selectedLanguage = preferences[PreferenceKeys.APP_LANGUAGE] ?: "en"
        )
    }

}