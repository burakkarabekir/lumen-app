package com.bksd.auth.presentation.signup

import androidx.compose.runtime.Immutable
import com.bksd.core.presentation.util.UiText

@Immutable
data class SignUpState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: UiText? = null,
) {
    val isSubmitEnabled: Boolean get() = fullName.isNotBlank() && email.isNotBlank() && password.length >= 8 && !isLoading
}
