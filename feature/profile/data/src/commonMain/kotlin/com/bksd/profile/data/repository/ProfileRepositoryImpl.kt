package com.bksd.profile.data.repository

import com.bksd.core.data.remote.supabase.SupabaseAuthDataSource
import com.bksd.core.data.remote.supabase.SupabaseBuckets
import com.bksd.core.data.remote.supabase.SupabaseStorageDataSource
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.NetworkErrorType
import com.bksd.core.domain.error.Result
import com.bksd.profile.data.remote.SupabaseProfileRemoteDataSource
import com.bksd.profile.domain.model.UserProfile
import com.bksd.profile.domain.repository.ProfileRepository
import kotlin.random.Random

class ProfileRepositoryImpl(
    private val authDataSource: SupabaseAuthDataSource,
    private val remoteDataSource: SupabaseProfileRemoteDataSource,
    private val storageDataSource: SupabaseStorageDataSource,
) : ProfileRepository {

    override suspend fun getUserProfile(): Result<UserProfile, AppError> {
        val uid = authDataSource.getSignedInUserId()
            ?: return Result.Error(AppError.Network(NetworkErrorType.UNAUTHORIZED))

        return when (val result = remoteDataSource.fetchProfile(uid)) {
            is Result.Success -> {
                val row = result.data
                Result.Success(
                    UserProfile(
                        displayName = row?.displayName ?: authDataSource.getDisplayName().orEmpty(),
                        photoUrl = row?.avatarUrl ?: authDataSource.getPhotoUrl(),
                        jobTitle = row?.jobTitle.orEmpty(),
                        joinYear = row?.joinYear.orEmpty(),
                        isPremium = row?.isPremium ?: false,
                    )
                )
            }

            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun uploadAvatar(bytes: ByteArray, mimeType: String?): Result<String, AppError> {
        val uid = authDataSource.getSignedInUserId()
            ?: return Result.Error(AppError.Network(NetworkErrorType.UNAUTHORIZED))

        val extension = when (mimeType) {
            "image/png" -> "png"
            "image/webp" -> "webp"
            else -> "jpg"
        }
        val path = "$uid/avatar_${Random.nextLong(0, Long.MAX_VALUE)}.$extension"

        return when (val result = storageDataSource.upload(SupabaseBuckets.AVATARS, path, bytes)) {
            is Result.Success -> {
                val publicUrl = storageDataSource.publicUrl(SupabaseBuckets.AVATARS, path)
                authDataSource.updatePhotoUrl(publicUrl)
                Result.Success(publicUrl)
            }

            is Result.Error -> Result.Error(result.error)
        }
    }
}
