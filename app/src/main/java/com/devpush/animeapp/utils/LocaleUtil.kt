package com.devpush.animeapp.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale


fun Context.applySelectedLanguage(languageCode: String): Context {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)

    val newConfig = Configuration(this.resources.configuration)
    newConfig.setLocale(locale)
    newConfig.setLayoutDirection(locale) // Important for RTL languages

    this.resources.updateConfiguration(newConfig, this.resources.displayMetrics) // Update activity resources

    // For API levels 17+ it's recommended to create a new context
    // return this.createConfigurationContext(newConfig)
    // However, for simplicity and focusing on recreate, we'll update current activity's resources directly.
    // The recreate() call will ensure everything is reloaded with the new default locale.
    return this
}