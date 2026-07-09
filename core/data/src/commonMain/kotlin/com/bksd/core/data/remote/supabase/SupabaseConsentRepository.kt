package com.bksd.core.data.remote.supabase

import com.bksd.core.domain.consent.ConsentRepository
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.legal.LegalConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class SupabaseConsentRepository(
    private val client: SupabaseClient,
) : ConsentRepository {

    @Serializable
    private data class ConsentDoc(val document: String)

    @Serializable
    private data class ConsentInsert(
        @SerialName("user_id") val userId: String,
        val document: String,
        val version: String,
        val method: String = "explicit",
    )

    override suspend fun needsConsent(): Boolean {
        val userId = client.auth.currentUserOrNull()?.id ?: return false
        return runCatching {
            client.postgrest["consent_records"]
                .select(Columns.list("document")) {
                    filter {
                        eq("user_id", userId)
                        eq("version", LegalConfig.POLICY_VERSION)
                        eq("document", "terms_of_service")
                    }
                }
                .decodeList<ConsentDoc>()
                .isEmpty()
        }.getOrDefault(false)
    }

    override suspend fun recordConsent(): Result<Unit, AppError> {
        val userId = client.auth.currentUserOrNull()?.id
            ?: return Result.Error(AppError.Unknown("not_authenticated"))
        return runCatching {
            client.postgrest["consent_records"].insert(
                listOf(
                    ConsentInsert(userId, "terms_of_service", LegalConfig.POLICY_VERSION),
                    ConsentInsert(userId, "privacy_policy", LegalConfig.POLICY_VERSION),
                )
            )
        }.fold(
            onSuccess = { Result.Success(Unit) },
            onFailure = { Result.Error(AppError.Unknown(it.message ?: "consent_record_failed")) },
        )
    }
}
