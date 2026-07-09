package com.bksd.auth.presentation.signup

import androidx.compose.runtime.Immutable
import com.bksd.auth.presentation.isValidEmail
import com.bksd.core.presentation.util.UiText

@Immutable
data class SignUpState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val agreedToTerms: Boolean = false,
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val awaitingConfirmation: Boolean = false,
) {
    val emailError: Boolean get() = email.isNotBlank() && !email.isValidEmail()
    val isSubmitEnabled: Boolean
        get() = fullName.isNotBlank() &&
            email.isValidEmail() &&
            password.length >= 8 &&
            agreedToTerms &&
            !isLoading
}
