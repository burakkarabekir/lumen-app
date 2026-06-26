package com.bksd.profile.data.remote

import com.bksd.core.data.remote.supabase.supabaseCall
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.profile.data.dto.UserProfileDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest

class SupabaseProfileRemoteDataSource(
    private val client: SupabaseClient,
) {
    suspend fun fetchProfile(userId: String): Result<UserProfileDto?, AppError> =
        supabaseCall {
            client.postgrest["profiles"]
                .select { filter { eq("id", userId) } }
                .decodeSingleOrNull<UserProfileDto>()
        }
}
