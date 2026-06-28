package com.bksd.profile.presentation

import com.bksd.core.presentation.util.UiText

sealed interface EditProfileEvent {
    data object NavigateBack : EditProfileEvent
    data object OpenPhotoPicker : EditProfileEvent
    data class ShowError(val error: UiText) : EditProfileEvent
}
