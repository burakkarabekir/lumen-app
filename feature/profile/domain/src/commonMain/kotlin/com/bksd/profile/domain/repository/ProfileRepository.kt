package com.bksd.profile.domain.repository

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.profile.domain.model.UserProfile

interface ProfileRepository {
    suspend fun getUserProfile(): Result<UserProfile, AppError>
    suspend fun uploadAvatar(bytes: ByteArray, mimeType: String?): Result<String, AppError>
    suspend fun clearUserData()
}
