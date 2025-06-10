package com.devpush.animeapp.utils

import androidx.datastore.preferences.core.booleanPreferencesKey

object PreferenceKeys {
    val IS_LOGIN = booleanPreferencesKey("is_login")
    val IS_ONBOARDING_SHOWN = booleanPreferencesKey("is_onboarding_shown")
}