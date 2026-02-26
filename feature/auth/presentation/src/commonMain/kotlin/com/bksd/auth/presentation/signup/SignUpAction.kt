package com.bksd.auth.presentation.signup

sealed interface SignUpAction {
    data class OnFullNameChange(val name: String) : SignUpAction
    data class OnEmailChange(val email: String) : SignUpAction
    data class OnPasswordChange(val password: String) : SignUpAction
    data object OnSignUpClick : SignUpAction
    data object OnSignInClick : SignUpAction
    data object OnTermsClick : SignUpAction
    data object OnPrivacyClick : SignUpAction
}
