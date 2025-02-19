package com.devpush.animeapp.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking


// Define the DataStore name
val ONBOARDING_PREFERENCES = "onboarding_preferences"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = ONBOARDING_PREFERENCES)

// Define the key for the boolean value
val IS_ONBOARDING_SHOWN = booleanPreferencesKey("is_onboarding_shown")

object DataStoreUtils {

    fun readOnboardingStatus(context: Context): Boolean {
        return runBlocking(Dispatchers.IO) { // Use Dispatchers.IO
            val preferences = context.dataStore.data.firstOrNull() // Use the property directly
            preferences?.get(IS_ONBOARDING_SHOWN) ?: false // Default to false
        }
    }

    suspend fun updateOnboardingStatus(context: Context, isShown: Boolean) {
        context.dataStore.edit { preferences -> // Use the property directly
            preferences[IS_ONBOARDING_SHOWN] = isShown
        }
    }

}