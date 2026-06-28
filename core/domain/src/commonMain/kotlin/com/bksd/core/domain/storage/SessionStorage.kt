package com.bksd.core.domain.storage

import kotlinx.coroutines.flow.Flow

interface SessionStorage {
    fun observeAuthState(): Flow<Boolean>
    fun isLoggedIn(): Boolean
    suspend fun awaitReady()
    fun getProfilePhotoUrl(): String?
    fun observeProfilePhotoUrl(): Flow<String?>
    suspend fun setRememberMe(enabled: Boolean)
    fun isRememberMeEnabled(): Flow<Boolean>

    suspend fun getLocalDataOwner(): String?
    suspend fun setLocalDataOwner(uid: String?)
}
