package com.bksd.core.data.applock

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.bksd.core.domain.applock.AppLockRepository
import com.bksd.core.domain.cleanup.LocalDataCleaner
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AppLockRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : AppLockRepository, LocalDataCleaner {

    private val enabledKey = booleanPreferencesKey("app_lock_enabled")

    override fun observeAppLockEnabled(): Flow<Boolean> =
        dataStore.data.map { prefs -> prefs[enabledKey] ?: false }

    override suspend fun isAppLockEnabled(): Boolean =
        dataStore.data.map { prefs -> prefs[enabledKey] ?: false }.first()

    override suspend fun setAppLockEnabled(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[enabledKey] = enabled }
    }

    override suspend fun clearLocalData() {
        dataStore.edit { prefs -> prefs.remove(enabledKey) }
    }
}
