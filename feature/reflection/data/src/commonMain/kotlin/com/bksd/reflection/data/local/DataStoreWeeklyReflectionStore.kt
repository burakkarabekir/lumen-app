package com.bksd.reflection.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.bksd.reflection.domain.model.WeeklyReflection
import com.bksd.reflection.domain.repository.WeeklyReflectionStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class DataStoreWeeklyReflectionStore(
    private val dataStore: DataStore<Preferences>
) : WeeklyReflectionStore {

    private val json = Json { ignoreUnknownKeys = true }

    override fun observe(): Flow<WeeklyReflection?> = dataStore.data.map { preferences ->
        preferences[KEY]?.let { stored ->
            runCatching { json.decodeFromString<WeeklyReflection>(stored) }.getOrNull()
        }
    }

    override suspend fun save(reflection: WeeklyReflection) {
        dataStore.edit { it[KEY] = json.encodeToString(reflection) }
    }

    override suspend fun clear() {
        dataStore.edit { it.remove(KEY) }
    }

    private companion object {
        val KEY = stringPreferencesKey("weekly_reflection")
    }
}
