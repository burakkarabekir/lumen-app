package com.bksd.profile.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    val jobTitle: String = "",
    val joinYear: String = "",
    val isPremium: Boolean = false
)
