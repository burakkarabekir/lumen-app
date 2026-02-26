package com.bksd.auth.presentation.signup

import com.bksd.core.presentation.util.UiText

sealed interface SignUpEvent {
    data object SignUpSuccess : SignUpEvent
    data class SignUpError(val error: UiText) : SignUpEvent
    data object NavigateToSignIn : SignUpEvent
    data object OpenTermsOfService : SignUpEvent
    data object OpenPrivacyPolicy : SignUpEvent
}
