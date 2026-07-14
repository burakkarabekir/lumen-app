package com.bksd.onboarding.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.bksd.onboarding.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OnboardingRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : OnboardingRepository {

    override fun observeCompleted(): Flow<Boolean> =
        dataStore.data.map { prefs -> prefs[COMPLETED] ?: false }

    override suspend fun setCompleted() {
        dataStore.edit { prefs -> prefs[COMPLETED] = true }
    }

    private companion object {
        val COMPLETED = booleanPreferencesKey("onboarding_completed")
    }
}
