package com.bksd.profile.domain.repository

import com.bksd.profile.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun getUserProfile(): UserProfile
    suspend fun uploadAvatar(bytes: ByteArray, mimeType: String?): String
    suspend fun clearUserData()
}
