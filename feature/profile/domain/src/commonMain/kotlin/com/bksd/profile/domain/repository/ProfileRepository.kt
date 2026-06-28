package com.bksd.profile.domain.repository

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.profile.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun getUserProfile(): Result<UserProfile, AppError>
    fun observeUserProfile(): Flow<Result<UserProfile, AppError>>
    suspend fun uploadAvatar(bytes: ByteArray, mimeType: String?): Result<String, AppError>
    suspend fun updateDisplayName(name: String): Result<Unit, AppError>
}
