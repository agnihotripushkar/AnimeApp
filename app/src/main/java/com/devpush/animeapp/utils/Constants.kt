package com.devpush.animeapp.utils

import androidx.datastore.preferences.core.booleanPreferencesKey

object Constants {

    // Nav Graph keys
    const val LOGIN_SCREEN = "login_screen"
    const val REGISTRATION_SCREEN = "registration_screen"
    const val ONBOARDING_SCREEN = "onBoarding_screen"
    const val TRENDING_ANIME_SCREEN = "trending_anime_screen"
    const val SETTINGS_SCREEN = "settings_screen"
    const val DETAIL_ANIME_SCREEN = "detail_anime_screen"

    // Define the key for the boolean value
    const val IS_ONBOARDING_SHOWN = "is_onboarding_shown"
    const val IS_LOGIN = "is_login"
    const val ANIME_PREFERENCES = "anime_preferences"

    const val BASE_URL = "https://kitsu.io/api/edge/"
    const val TIMEOUT = 15000L

}