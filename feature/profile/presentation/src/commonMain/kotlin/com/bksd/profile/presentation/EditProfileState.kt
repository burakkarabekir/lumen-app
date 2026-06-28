package com.bksd.profile.presentation

data class EditProfileState(
    val name: String = "",
    val photoUrl: String? = null,
    val isLoading: Boolean = true,
    val isAvatarLoading: Boolean = false,
    val isSaving: Boolean = false,
)
