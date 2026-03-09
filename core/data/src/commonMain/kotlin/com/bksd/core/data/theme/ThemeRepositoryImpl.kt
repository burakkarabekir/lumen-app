package com.bksd.core.data.theme

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.bksd.core.domain.theme.AppThemeMode
import com.bksd.core.domain.theme.ThemeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemeRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : ThemeRepository {

    private companion object {
        val THEME_KEY = stringPreferencesKey("app_theme")
    }

    override fun observeTheme(): Flow<AppThemeMode> {
        return dataStore.data.map { prefs ->
            val value = prefs[THEME_KEY]
            when (value) {
                "LIGHT" -> AppThemeMode.LIGHT
                "DARK" -> AppThemeMode.DARK
                else -> AppThemeMode.SYSTEM
            }
        }
    }

    override suspend fun setTheme(mode: AppThemeMode) {
        dataStore.edit { prefs ->
            prefs[THEME_KEY] = mode.name
        }
    }
}
