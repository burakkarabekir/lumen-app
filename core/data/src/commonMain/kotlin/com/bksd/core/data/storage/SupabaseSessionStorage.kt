package com.bksd.core.data.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.bksd.core.data.remote.supabase.SupabaseAuthDataSource
import com.bksd.core.domain.storage.SessionStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SupabaseSessionStorage(
    private val authDataSource: SupabaseAuthDataSource,
    private val dataStore: DataStore<Preferences>,
) : SessionStorage {

    override fun observeAuthState(): Flow<Boolean> =
        authDataSource.authState

    override fun isLoggedIn(): Boolean =
        authDataSource.getSignedInUserId() != null

    override suspend fun awaitReady() = authDataSource.awaitInitialization()

    override fun getProfilePhotoUrl(): String? =
        authDataSource.getPhotoUrl()

    override suspend fun setRememberMe(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[REMEMBER_ME] = enabled
        }
    }

    override fun isRememberMeEnabled(): Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[REMEMBER_ME] ?: false
        }

    override suspend fun getLocalDataOwner(): String? =
        dataStore.data.map { preferences -> preferences[LOCAL_DATA_OWNER] }.first()

    override suspend fun setLocalDataOwner(uid: String?) {
        dataStore.edit { preferences ->
            if (uid == null) preferences.remove(LOCAL_DATA_OWNER)
            else preferences[LOCAL_DATA_OWNER] = uid
        }
    }

    companion object {
        private val REMEMBER_ME = booleanPreferencesKey("remember_me")
        private val LOCAL_DATA_OWNER = stringPreferencesKey("local_data_owner")
    }
}
