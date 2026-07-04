package com.bksd.core.data.remote.supabase

import com.bksd.core.domain.billing.PremiumStatusSource
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class SupabasePremiumStatusSource(
    private val client: SupabaseClient,
) : PremiumStatusSource {

    @Serializable
    private data class PremiumRow(
        @SerialName("is_premium") val isPremium: Boolean = false,
    )

    override suspend fun isServerPremium(): Boolean {
        val userId = client.auth.currentUserOrNull()?.id ?: return false
        return runCatching {
            client.postgrest["profiles"]
                .select(Columns.list("is_premium")) { filter { eq("id", userId) } }
                .decodeSingleOrNull<PremiumRow>()
                ?.isPremium ?: false
        }.getOrDefault(false)
    }
}
