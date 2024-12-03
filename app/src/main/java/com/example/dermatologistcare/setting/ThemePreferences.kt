package com.example.dermatologistcare.setting


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ThemePreferences(private val context: Context) {
    private val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")

    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_DARK_MODE] ?: false
        }

    suspend fun toggleTheme() {
        context.dataStore.edit { preferences ->
            val current = preferences[IS_DARK_MODE] ?: false
            preferences[IS_DARK_MODE] = !current
        }
    }
}
