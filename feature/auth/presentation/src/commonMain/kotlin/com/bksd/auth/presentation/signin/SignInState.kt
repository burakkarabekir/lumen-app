package com.bksd.auth.presentation.signin

import androidx.compose.runtime.Immutable
import com.bksd.core.presentation.util.UiText

@Immutable
data class SignInState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: UiText? = null,
) {
    val isSubmitEnabled: Boolean get() = email.isNotBlank() && password.isNotBlank() && !isLoading
}
