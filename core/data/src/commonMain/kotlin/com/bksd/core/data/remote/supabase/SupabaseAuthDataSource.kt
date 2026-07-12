package com.bksd.core.data.remote.supabase

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.legal.LegalConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.SignOutScope
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Apple
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.functions.functions
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.put

class SupabaseAuthDataSource(
    private val client: SupabaseClient,
) {
    private val auth get() = client.auth

    val authState: Flow<Boolean> =
        auth.sessionStatus
            .mapNotNull { it.toAuthStateOrNull() }
            .distinctUntilChanged()

    suspend fun signIn(email: String, password: String): Result<Unit, AppError> =
        supabaseCall {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
        }

    suspend fun signUp(
        email: String,
        password: String,
        fullName: String,
    ): Result<Unit, AppError> = supabaseCall {
        auth.signUpWith(Email) {
            this.email = email
            this.password = password
            this.data = buildJsonObject {
                put("full_name", fullName)
                put("policy_version", LegalConfig.POLICY_VERSION)
            }
        }
        Unit
    }

    suspend fun signOut(): Result<Unit, AppError> = supabaseCall { auth.signOut() }

    suspend fun deleteAccount(): Result<Unit, AppError> = supabaseCall {
        val response = client.functions.invoke("delete-account")
        if (!response.status.isSuccess()) {
            throw IllegalStateException("delete_account_failed_${response.status.value}")
        }
        runCatching { auth.signOut(SignOutScope.LOCAL) }
        Unit
    }

    suspend fun resetPassword(email: String): Result<Unit, AppError> =
        supabaseCall { auth.resetPasswordForEmail(email) }

    suspend fun signInWithGoogle(idToken: String): Result<Unit, AppError> =
        supabaseCall {
            auth.signInWith(IDToken) {
                this.idToken = idToken
                provider = Google
            }
        }

    suspend fun signInWithApple(idToken: String, nonce: String?): Result<Unit, AppError> =
        supabaseCall {
            auth.signInWith(IDToken) {
                this.idToken = idToken
                this.nonce = nonce
                provider = Apple
            }
        }

    fun getSignedInUserId(): String? = auth.currentUserOrNull()?.id

    suspend fun awaitInitialization() = auth.awaitInitialization()

    fun getDisplayName(): String? = userMetaString("full_name")

    fun getPhotoUrl(): String? = userMetaString("avatar_url")

    fun getJoinYear(): String? =
        auth.currentUserOrNull()?.createdAt?.toString()?.take(4)?.takeIf { it.all(Char::isDigit) }

    fun observePhotoUrl(): Flow<String?> =
        auth.sessionStatus.map { getPhotoUrl() }.distinctUntilChanged()

    fun observeIdentityChanges(): Flow<Unit> =
        auth.sessionStatus
            .map { getDisplayName() to getPhotoUrl() }
            .distinctUntilChanged()
            .map { }

    suspend fun updateDisplayName(name: String): Result<Unit, AppError> =
        supabaseCall {
            auth.updateUser { data { put("full_name", name) } }
            Unit
        }

    suspend fun updatePhotoUrl(url: String): Result<Unit, AppError> =
        supabaseCall {
            auth.updateUser { data { put("avatar_url", url) } }
            Unit
        }

    private fun userMetaString(key: String): String? =
        (auth.currentUserOrNull()?.userMetadata?.get(key) as? JsonPrimitive)?.contentOrNull
}

internal fun SessionStatus.toAuthStateOrNull(): Boolean? = when (this) {
    is SessionStatus.Authenticated,
    is SessionStatus.RefreshFailure -> true
    is SessionStatus.NotAuthenticated -> false
    is SessionStatus.Initializing -> null
}
