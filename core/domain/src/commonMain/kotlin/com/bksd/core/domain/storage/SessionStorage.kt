package com.bksd.core.domain.storage

import kotlinx.coroutines.flow.Flow

interface SessionStorage {
    fun observeAuthState(): Flow<Boolean>
    fun isLoggedIn(): Boolean
    suspend fun set(isLoggedIn: Boolean)
}
