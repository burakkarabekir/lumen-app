package com.bksd.profile.presentation

data class LockPrivacyState(
    val appLockEnabled: Boolean = false,
    val policyVersion: String = "",
)
