package com.devpush.animeapp.domian.model

data class UserPreferences(
    val isLogin: Boolean,
    val isOnboardingShown: Boolean,
    val selectedTheme: String = "system",
    val selectedLanguage: String = "en"
)
