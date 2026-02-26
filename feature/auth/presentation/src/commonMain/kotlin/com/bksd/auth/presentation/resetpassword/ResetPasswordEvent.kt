package com.bksd.auth.presentation.resetpassword

import com.bksd.core.presentation.util.UiText

sealed interface ResetPasswordEvent {
    data object ResetPasswordSuccess : ResetPasswordEvent
    data class ResetPasswordError(val error: UiText) : ResetPasswordEvent
    data object NavigateToSignIn : ResetPasswordEvent
}
