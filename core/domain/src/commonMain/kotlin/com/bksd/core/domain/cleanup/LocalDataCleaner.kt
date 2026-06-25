package com.bksd.core.domain.cleanup

interface LocalDataCleaner {
    suspend fun clearLocalData()
}
