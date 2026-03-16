package com.bksd.profile.domain.model

data class UserProfile(
    val displayName: String,
    val photoUrl: String?,
    val jobTitle: String,
    val joinYear: String,
    val isPremium: Boolean
)
