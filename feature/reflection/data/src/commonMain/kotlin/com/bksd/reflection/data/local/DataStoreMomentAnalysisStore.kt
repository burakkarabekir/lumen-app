package com.bksd.reflection.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.bksd.reflection.domain.model.EntryAnalysis
import com.bksd.reflection.domain.model.MomentAnalysisState
import com.bksd.reflection.domain.model.MomentReflection
import com.bksd.reflection.domain.repository.MomentAnalysisStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.time.Clock

@Serializable
internal enum class AnalysisStatus { PENDING, READY, FAILED, OFFLINE, QUOTA_EXCEEDED }

@Serializable
internal data class StoredAnalysis(
    val status: AnalysisStatus,
    val reflection: MomentReflection? = null,
    val savedAtMs: Long = 0L
)

class DataStoreMomentAnalysisStore(
    private val dataStore: DataStore<Preferences>,
    private val clock: Clock,
) : MomentAnalysisStore {

    private val json = Json { ignoreUnknownKeys = true }

    override fun observe(momentId: String): Flow<MomentAnalysisState> =
        dataStore.data.map { preferences -> readMap(preferences)[momentId].toState() }

    override suspend fun setPending(momentId: String) = put(momentId, AnalysisStatus.PENDING, null)

    override suspend fun setResult(momentId: String, reflection: MomentReflection) =
        put(momentId, AnalysisStatus.READY, reflection)

    override suspend fun setFailed(momentId: String, quotaExceeded: Boolean) =
        put(momentId, if (quotaExceeded) AnalysisStatus.QUOTA_EXCEEDED else AnalysisStatus.FAILED, null)

    override suspend fun setOffline(momentId: String) = put(momentId, AnalysisStatus.OFFLINE, null)

    override suspend fun recentAnalyses(limit: Int): List<EntryAnalysis> =
        readMap(dataStore.data.first())
            .values
            .filter { it.status == AnalysisStatus.READY }
            .sortedByDescending { it.savedAtMs }
            .take(limit)
            .mapNotNull { it.reflection?.analysis }

    override suspend fun deleteAll() {
        dataStore.edit { it.remove(KEY) }
    }

    private suspend fun put(momentId: String, status: AnalysisStatus, reflection: MomentReflection?) {
        dataStore.edit { preferences ->
            val map = readMap(preferences).toMutableMap()
            map[momentId] = StoredAnalysis(status, reflection, clock.now().toEpochMilliseconds())
            preferences[KEY] = json.encodeToString(map)
        }
    }

    private fun readMap(preferences: Preferences): Map<String, StoredAnalysis> =
        preferences[KEY]?.let {
            runCatching { json.decodeFromString<Map<String, StoredAnalysis>>(it) }.getOrNull()
        } ?: emptyMap()

    private fun StoredAnalysis?.toState(): MomentAnalysisState = when {
        this == null -> MomentAnalysisState.None
        status == AnalysisStatus.PENDING -> MomentAnalysisState.Pending
        status == AnalysisStatus.QUOTA_EXCEEDED -> MomentAnalysisState.QuotaExceeded
        status == AnalysisStatus.OFFLINE -> MomentAnalysisState.Offline
        status == AnalysisStatus.FAILED -> MomentAnalysisState.Failed
        reflection != null -> MomentAnalysisState.Ready(reflection)
        else -> MomentAnalysisState.None
    }

    private companion object {
        val KEY = stringPreferencesKey("moment_analyses")
    }
}
