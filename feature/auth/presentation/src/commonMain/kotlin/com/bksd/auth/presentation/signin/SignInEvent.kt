package com.bksd.auth.presentation.signin

import com.bksd.core.presentation.util.UiText

sealed interface SignInEvent {
    data object SignInSuccess : SignInEvent
    data class SignInError(val error: UiText) : SignInEvent
    data object NavigateToSignUp : SignInEvent
    data object NavigateToForgotPassword : SignInEvent
}
