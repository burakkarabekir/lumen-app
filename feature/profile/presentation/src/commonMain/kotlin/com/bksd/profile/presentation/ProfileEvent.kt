package com.bksd.profile.presentation

import com.bksd.core.presentation.util.UiText

sealed interface ProfileEvent {
    data object SignOutSuccess : ProfileEvent
    data object NavigateToPaywall : ProfileEvent
    data object NavigateToEditProfile : ProfileEvent
    data object OpenPhotoPicker : ProfileEvent
    data class PermissionError(val error: UiText) : ProfileEvent
    data class SignOutError(val error: UiText) : ProfileEvent
}
