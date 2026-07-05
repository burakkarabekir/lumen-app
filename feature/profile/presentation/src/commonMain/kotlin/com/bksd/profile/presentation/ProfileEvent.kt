package com.bksd.profile.presentation

import com.bksd.core.presentation.util.UiText

sealed interface ProfileEvent {
    data object SignOutSuccess : ProfileEvent
    data object DeleteAccountSuccess : ProfileEvent
    data object NavigateToPaywall : ProfileEvent
    data object NavigateToManagePremium : ProfileEvent
    data object NavigateToCloudSync : ProfileEvent
    data object NavigateToLockPrivacy : ProfileEvent
    data object NavigateToExportJournal : ProfileEvent
    data object NavigateToLegal : ProfileEvent
    data object NavigateToEditProfile : ProfileEvent
    data object NavigateToAbout : ProfileEvent
    data object NavigateToHelp : ProfileEvent
    data object OpenPhotoPicker : ProfileEvent
    data class PermissionError(val error: UiText) : ProfileEvent
    data class SignOutError(val error: UiText) : ProfileEvent
    data class DeleteAccountError(val error: UiText) : ProfileEvent
}
