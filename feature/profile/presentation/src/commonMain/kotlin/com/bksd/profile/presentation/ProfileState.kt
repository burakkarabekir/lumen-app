package com.bksd.profile.presentation

data class ProfileState(
    val name: String = "",
    val photoUrl: String? = null,
    val jobTitle: String = "",
    val joinYear: String = "",
    val isPremium: Boolean = false,
    val isProfileLoading: Boolean = true,
    val isAvatarLoading: Boolean = false,
    val hasNotificationBadge: Boolean = false,
    val isSigningOut: Boolean = false,
)
