package com.bksd.profile.domain.repository

import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun observeAvatarUrl(): Flow<String?>
    suspend fun setAvatarUrl(url: String?)
    suspend fun saveAvatarImage(bytes: ByteArray, mimeType: String?): String
}
