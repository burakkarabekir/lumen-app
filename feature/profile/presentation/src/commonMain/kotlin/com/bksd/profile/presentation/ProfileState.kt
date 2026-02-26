package com.bksd.profile.presentation

/**
 * Immutable UI state for the User Profile screen.
 * All types are stable for Compose recomposition safety.
 */
data class ProfileState(
    val displayName: String = "",
    val role: String = "",
    val memberSince: String = "",
    val avatarUrl: String? = null,
    val currentTheme: String = "Dark Mode",
    val hasNotificationBadge: Boolean = false,
    val isSigningOut: Boolean = false
)
