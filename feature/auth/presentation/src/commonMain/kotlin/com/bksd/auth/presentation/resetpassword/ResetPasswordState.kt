package com.bksd.auth.presentation.resetpassword

import androidx.compose.runtime.Immutable
import com.bksd.core.presentation.util.UiText

@Immutable
data class ResetPasswordState(
    val email: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: UiText? = null,
) {
    val isSubmitEnabled: Boolean get() = email.isNotBlank() && !isLoading
}
