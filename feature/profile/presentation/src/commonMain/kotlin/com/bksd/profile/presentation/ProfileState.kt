package com.bksd.profile.presentation

/**
 * Immutable UI state for the User Profile screen.
 * All types are stable for Compose recomposition safety.
 *
 * Theme is NOT owned here — it's accessed via [LocalThemeController].
 */
data class ProfileState(
    val displayName: String = "",
    val role: String = "",
    val memberSince: String = "",
    val avatarUrl: String? = null,
    val isAvatarLoading: Boolean = false,
    val hasNotificationBadge: Boolean = false,
    val isSigningOut: Boolean = false,
)
