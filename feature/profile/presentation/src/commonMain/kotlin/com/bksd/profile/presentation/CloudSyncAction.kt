package com.bksd.profile.presentation

sealed interface CloudSyncAction {
    data object OnSyncNowClick : CloudSyncAction
}
