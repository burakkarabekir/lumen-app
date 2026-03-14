package com.bksd.core.data.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.bksd.core.domain.storage.SessionStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class DataStoreSessionStorage(
    private val dataStore: DataStore<Preferences>
) : SessionStorage {

    private val authKey = booleanPreferencesKey("KEY_IS_LOGGED_IN")

    override fun observeAuthState(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[authKey] ?: false
        }
    }

    override fun isLoggedIn(): Boolean {
        return runBlocking {
            dataStore.data.first()[authKey] ?: false
        }
    }

    override suspend fun set(isLoggedIn: Boolean) {
        dataStore.edit { prefs ->
            prefs[authKey] = isLoggedIn
        }
    }
}
