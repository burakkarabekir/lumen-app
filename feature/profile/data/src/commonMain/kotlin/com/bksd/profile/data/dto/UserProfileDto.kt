package com.bksd.profile.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    @SerialName("display_name") val displayName: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("job_title") val jobTitle: String = "",
    @SerialName("join_year") val joinYear: String = "",
    @SerialName("is_premium") val isPremium: Boolean = false,
)
