package com.devpush.animeapp.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.ANIME_PREFERNCES)

object DataStoreUtils {

    // Function to read any String value from DataStore
    fun readStringValue(context: Context, key: String, defaultValue: String? = null): String? {
        return runBlocking(Dispatchers.IO) {
            val preferences = context.dataStore.data.firstOrNull()
            preferences?.get(stringPreferencesKey(key)) ?: defaultValue
        }
    }

    // Function to update any String value in DataStore
    suspend fun updateStringValue(context: Context, key: String, value: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    //Function to read any Boolean value from Datastore.
    fun readBooleanValue(context: Context, key: String, defaultValue: Boolean = false): Boolean{
        return runBlocking(Dispatchers.IO){
            val preferences = context.dataStore.data.firstOrNull()
            preferences?.get(booleanPreferencesKey(key)) ?: defaultValue
        }
    }

    //Function to update any Boolean value in Datastore.
    suspend fun updateBooleanValue(context: Context, key: String, value: Boolean){
        context.dataStore.edit{preferences ->
            preferences[booleanPreferencesKey(key)] = value
        }
    }

}