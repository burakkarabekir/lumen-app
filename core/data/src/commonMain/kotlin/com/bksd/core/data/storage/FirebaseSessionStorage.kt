package com.bksd.core.data.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.bksd.core.data.remote.firebase.FirebaseAuthDataSource
import com.bksd.core.domain.storage.SessionStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FirebaseSessionStorage(
    private val firebaseAuthDataSource: FirebaseAuthDataSource,
    private val dataStore: DataStore<Preferences>,
) : SessionStorage {

    override fun observeAuthState(): Flow<Boolean> =
        firebaseAuthDataSource.authState

    override fun isLoggedIn(): Boolean =
        firebaseAuthDataSource.getSignedInUserId() != null

    override suspend fun setRememberMe(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[REMEMBER_ME] = enabled
        }
    }

    override fun isRememberMeEnabled(): Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[REMEMBER_ME] ?: false
        }

    companion object {
        private val REMEMBER_ME = booleanPreferencesKey("remember_me")
    }
}
