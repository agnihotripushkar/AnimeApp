package com.devpush.animeapp.utils

import androidx.datastore.preferences.core.booleanPreferencesKey

object Constants {
    // Define the key for the boolean value
    const val IS_ONBOARDING_SHOWN = "is_onboarding_shown"
    const val IS_LOGIN = "is_login"
    const val ANIME_PREFERENCES = "anime_preferences"

    const val BASE_URL = "https://kitsu.io/api/edge/"
    const val TIMEOUT = 15000L

}