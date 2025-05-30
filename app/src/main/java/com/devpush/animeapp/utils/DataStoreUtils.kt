package com.devpush.animeapp.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first

object DataStoreUtils {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.ANIME_PREFERENCES)

    suspend fun saveBooleanValue(context: Context, key: String, value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(key)] = value
        }
    }

    // Modified to provide a default value of 'false'
    fun getBooleanFlow(context: Context, key: String): Flow<Boolean> {
        return context.dataStore.data
            .map { preferences ->
                preferences[booleanPreferencesKey(key)] ?: false // Default to false
            }
    }

    private fun getBooleanKey(name: String): Preferences.Key<Boolean> = booleanPreferencesKey(name)


    suspend fun updateBooleanValue(context: Context, key: String, value: Boolean) {
        val dataStoreKey = getBooleanKey(key)
        context.dataStore.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }

    // Modified to provide a default value of 'false'
    suspend fun readBooleanValue(context: Context, key: String): Boolean {
        val dataStoreKey = getBooleanKey(key)
        // Use .first() and provide a default directly in the map operation
        return context.dataStore.data.map { it[dataStoreKey] ?: false }.first()
    }


    fun observeBooleanPreference(context: Context, key: String): Flow<Boolean> {
        val dataStoreKey = getBooleanKey(key)
        return context.dataStore.data.map { preferences ->
            preferences[dataStoreKey] ?: false // Default to false
        }
    }
}