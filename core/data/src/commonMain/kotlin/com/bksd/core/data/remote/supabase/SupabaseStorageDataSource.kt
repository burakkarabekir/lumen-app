package com.bksd.core.data.remote.supabase

import com.bksd.core.data.media.readFileBytes
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import kotlin.time.Duration

object SupabaseBuckets {
    const val AVATARS = "avatars"
    const val MEDIA = "media"
}

class SupabaseStorageDataSource(
    private val client: SupabaseClient,
) {
    suspend fun upload(bucket: String, path: String, bytes: ByteArray): Result<Unit, AppError> =
        supabaseCall {
            client.storage.from(bucket).upload(path, bytes) { upsert = true }
            Unit
        }

    suspend fun uploadFromPath(bucket: String, path: String, localPath: String): Result<Unit, AppError> =
        upload(bucket, path, readFileBytes(localPath))

    fun publicUrl(bucket: String, path: String): String =
        client.storage.from(bucket).publicUrl(path)

    suspend fun signedUrl(bucket: String, path: String, expiresIn: Duration): Result<String, AppError> =
        supabaseCall {
            client.storage.from(bucket).createSignedUrl(path, expiresIn)
        }

    suspend fun delete(bucket: String, path: String): Result<Unit, AppError> =
        supabaseCall {
            client.storage.from(bucket).delete(path)
            Unit
        }
}
