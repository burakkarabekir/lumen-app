package com.bksd.core.data.remote.supabase

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.bksd.core.domain.billing.PremiumStatusSource
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.first
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.coroutines.cancellation.CancellationException

class SupabasePremiumStatusSource(
    private val client: SupabaseClient,
    private val dataStore: DataStore<Preferences>,
) : PremiumStatusSource {

    @Serializable
    private data class PremiumRow(
        @SerialName("is_premium") val isPremium: Boolean = false,
    )

    override suspend fun isServerPremium(): Boolean {
        val userId = client.auth.currentUserOrNull()?.id ?: return false
        return try {
            val value = client.postgrest["profiles"]
                .select(Columns.list("is_premium")) { filter { eq("id", userId) } }
                .decodeSingleOrNull<PremiumRow>()
                ?.isPremium ?: false
            cache(userId, value)
            value
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (_: Exception) {
            // Offline / network failure — fall back to the last value we saw for this user
            // so server-granted premium survives without a connection.
            cachedFor(userId)
        }
    }

    private suspend fun cache(userId: String, value: Boolean) {
        dataStore.edit { preferences ->
            preferences[CACHED_UID] = userId
            preferences[CACHED_VALUE] = value
        }
    }

    private suspend fun cachedFor(userId: String): Boolean {
        val preferences = dataStore.data.first()
        return if (preferences[CACHED_UID] == userId) preferences[CACHED_VALUE] ?: false else false
    }

    private companion object {
        private val CACHED_UID = stringPreferencesKey("cached_premium_uid")
        private val CACHED_VALUE = booleanPreferencesKey("cached_premium_value")
    }
}
