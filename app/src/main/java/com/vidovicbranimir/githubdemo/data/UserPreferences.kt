package com.vidovicbranimir.githubdemo.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.clear
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(
    context: Context
) {
    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences>

    init {
        dataStore = applicationContext.createDataStore(
            name = "user_settings"
        )
    }


    val authToken: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_AUTH] ?: ""
        }


    suspend fun saveAuthToken(authToken: String) {
        dataStore.edit { preferences ->
            preferences[KEY_AUTH] = authToken
        }
    }

    suspend fun saveString(key: String, value: String) {
        dataStore.edit { preferences ->
            preferences[preferencesKey<String>(key)] = value
        }
    }

    fun getString(key: String): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[preferencesKey<String>(key)] ?: ""
        }

    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val KEY_AUTH = preferencesKey<String>("key_auth")
    }

}