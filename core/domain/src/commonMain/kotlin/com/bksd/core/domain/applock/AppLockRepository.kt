package com.bksd.core.domain.applock

import kotlinx.coroutines.flow.Flow

interface AppLockRepository {
    fun observeAppLockEnabled(): Flow<Boolean>
    suspend fun isAppLockEnabled(): Boolean
    suspend fun setAppLockEnabled(enabled: Boolean)
}
