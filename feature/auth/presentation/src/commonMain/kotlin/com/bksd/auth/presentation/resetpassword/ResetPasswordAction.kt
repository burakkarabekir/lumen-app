package com.bksd.auth.presentation.resetpassword

sealed interface ResetPasswordAction {
    data class OnEmailChange(val email: String) : ResetPasswordAction
    data object OnSubmitClick : ResetPasswordAction
    data object OnBackToSignInClick : ResetPasswordAction
}
