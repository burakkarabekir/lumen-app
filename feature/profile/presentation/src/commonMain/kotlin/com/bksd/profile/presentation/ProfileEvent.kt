package com.bksd.profile.presentation

sealed interface ProfileEvent {
    data object SignOutSuccess : ProfileEvent
    data object NavigateToPaywall : ProfileEvent
    data object OpenPhotoPicker : ProfileEvent
    data class PermissionError(val message: String) : ProfileEvent
    data class SignOutError(val message: String) : ProfileEvent
}
