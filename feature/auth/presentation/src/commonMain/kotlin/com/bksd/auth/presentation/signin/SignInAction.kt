package com.bksd.auth.presentation.signin

sealed interface SignInAction {
    data class OnEmailChange(val email: String) : SignInAction
    data class OnPasswordChange(val password: String) : SignInAction
    data class OnRememberMeToggle(val enabled: Boolean) : SignInAction
    data object OnSignInClick : SignInAction
    data object OnSignUpClick : SignInAction
    data object OnForgotPasswordClick : SignInAction
    data class OnGoogleIdTokenReceived(val idToken: String) : SignInAction
    data class OnGoogleSignInFailed(val cancelled: Boolean) : SignInAction
}
