package com.bksd.profile.presentation

sealed interface LockPrivacyAction {
    data class OnSetAppLock(val enabled: Boolean) : LockPrivacyAction
}
