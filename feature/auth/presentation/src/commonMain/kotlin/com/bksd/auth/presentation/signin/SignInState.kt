package com.bksd.auth.presentation.signin

import androidx.compose.runtime.Immutable
import com.bksd.auth.presentation.isValidEmail
import com.bksd.core.presentation.util.UiText

@Immutable
data class SignInState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSocialLoading: Boolean = false,
    val error: UiText? = null,
    val rememberMe: Boolean = false,
) {
    val emailError: Boolean get() = email.isNotBlank() && !email.isValidEmail()
    val isSubmitEnabled: Boolean
        get() = email.isValidEmail() && password.isNotBlank() && !isLoading && !isSocialLoading
}
