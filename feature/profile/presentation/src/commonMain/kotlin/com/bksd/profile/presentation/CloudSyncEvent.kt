package com.bksd.profile.presentation

import com.bksd.core.presentation.util.UiText

sealed interface CloudSyncEvent {
    data object SyncSuccess : CloudSyncEvent
    data class SyncError(val error: UiText) : CloudSyncEvent
}
