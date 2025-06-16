package com.devpush.animeapp.utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val IS_LOGIN = booleanPreferencesKey("is_login")
    val IS_ONBOARDING_SHOWN = booleanPreferencesKey("is_onboarding_shown")
    val APP_THEME = stringPreferencesKey("app_theme")
    val APP_LANGUAGE = stringPreferencesKey("app_language")
}