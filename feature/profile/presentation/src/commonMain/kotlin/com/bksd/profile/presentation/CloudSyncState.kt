package com.bksd.profile.presentation

data class CloudSyncState(
    val accountName: String = "",
    val entriesCount: Int = 0,
    val isSyncing: Boolean = false,
)
