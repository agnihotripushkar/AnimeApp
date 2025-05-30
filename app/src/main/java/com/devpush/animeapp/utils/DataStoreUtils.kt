package com.devpush.animeapp.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

// In DataStoreUtils.kt
object DataStoreUtils {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    suspend fun saveBooleanValue(context: Context, key: String, value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(key)] = value
        }
    }

    fun getBooleanFlow(context: Context, key: String): Flow<Boolean?> {
        return context.dataStore.data
            .map { preferences ->
                preferences[booleanPreferencesKey(key)]
            }
    }

    private fun getBooleanKey(name: String): Preferences.Key<Boolean> = booleanPreferencesKey(name)


    suspend fun updateBooleanValue(context: Context, key: String, value: Boolean) {
        val dataStoreKey = getBooleanKey(key)
        context.dataStore.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }

    suspend fun readBooleanValue(context: Context, key: String): Boolean? {
        val dataStoreKey = getBooleanKey(key)
        val preferences = context.dataStore.data.map { it[dataStoreKey] }
        return preferences.firstOrNull()
    }

    fun observeBooleanPreference(context: Context, key: String): Flow<Boolean?> {
        val dataStoreKey = getBooleanKey(key)
        return context.dataStore.data.map { preferences ->
            preferences[dataStoreKey]
        }
    }
}