package com.bksd.journal.data.remote

import com.bksd.core.data.remote.supabase.SupabaseAuthDataSource
import com.bksd.core.data.remote.supabase.supabaseCall
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.NetworkErrorType
import com.bksd.core.domain.error.Result
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order

class SupabaseMomentRemoteDataSource(
    private val client: SupabaseClient,
    private val authDataSource: SupabaseAuthDataSource,
) {
    private val moments get() = client.postgrest["moments"]

    suspend fun fetchMomentsPaged(limit: Int, offset: Int): Result<List<MomentDto>, AppError> {
        val uid = authDataSource.getSignedInUserId()
            ?: return Result.Error(AppError.Network(NetworkErrorType.UNAUTHORIZED))
        return supabaseCall {
            moments.select(Columns.raw(MOMENT_COLUMNS)) {
                filter { eq("user_id", uid) }
                order("created_at_ms", Order.DESCENDING)
                range(offset.toLong(), (offset + limit - 1).toLong())
            }.decodeList<MomentDto>()
        }
    }

    suspend fun fetchMoment(id: String): Result<MomentDto?, AppError> {
        val uid = authDataSource.getSignedInUserId()
            ?: return Result.Error(AppError.Network(NetworkErrorType.UNAUTHORIZED))
        return supabaseCall {
            moments.select(Columns.raw(MOMENT_COLUMNS)) {
                filter {
                    eq("user_id", uid)
                    eq("id", id)
                }
            }.decodeSingleOrNull<MomentDto>()
        }
    }

    suspend fun saveMoment(dto: MomentDto): Result<Unit, AppError> {
        val uid = authDataSource.getSignedInUserId()
            ?: return Result.Error(AppError.Network(NetworkErrorType.UNAUTHORIZED))
        return supabaseCall {
            moments.upsert(dto.copy(userId = uid))
            Unit
        }
    }

    suspend fun deleteMoment(id: String): Result<Unit, AppError> {
        val uid = authDataSource.getSignedInUserId()
            ?: return Result.Error(AppError.Network(NetworkErrorType.UNAUTHORIZED))
        return supabaseCall {
            moments.delete {
                filter {
                    eq("user_id", uid)
                    eq("id", id)
                }
            }
            Unit
        }
    }

    private companion object {
        const val MOMENT_COLUMNS =
            "id,user_id,title,body,created_at_ms,moods,tags,location,attachments"
    }
}
