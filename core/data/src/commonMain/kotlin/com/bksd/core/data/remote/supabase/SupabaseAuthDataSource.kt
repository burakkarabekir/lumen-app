package com.bksd.core.data.remote.supabase

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.put

class SupabaseAuthDataSource(
    private val client: SupabaseClient,
) {
    private val auth get() = client.auth

    val authState: Flow<Boolean> =
        auth.sessionStatus.map { it is SessionStatus.Authenticated }

    suspend fun signIn(email: String, password: String): Result<Unit, AppError> =
        supabaseCall {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            Unit
        }

    suspend fun signUp(
        email: String,
        password: String,
        fullName: String,
    ): Result<Unit, AppError> = supabaseCall {
        auth.signUpWith(Email) {
            this.email = email
            this.password = password
            this.data = buildJsonObject { put("full_name", fullName) }
        }
        Unit
    }

    suspend fun signOut(): Result<Unit, AppError> = supabaseCall { auth.signOut() }

    suspend fun resetPassword(email: String): Result<Unit, AppError> =
        supabaseCall { auth.resetPasswordForEmail(email) }

    suspend fun signInWithGoogle(idToken: String): Result<Unit, AppError> =
        supabaseCall {
            auth.signInWith(IDToken) {
                this.idToken = idToken
                provider = Google
            }
            Unit
        }

    fun getSignedInUserId(): String? = auth.currentUserOrNull()?.id

    fun getDisplayName(): String? = userMetaString("full_name")

    fun getPhotoUrl(): String? = userMetaString("avatar_url")

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
