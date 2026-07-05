package com.bksd.core.presentation.auth

import androidx.compose.runtime.Composable

sealed interface AppleSignInResult {
    data class Success(val idToken: String, val nonce: String?) : AppleSignInResult
    data object Cancelled : AppleSignInResult
    data class Failed(val message: String) : AppleSignInResult
}

fun interface AppleSignInLauncher {
    fun launch()
}

@Composable
expect fun rememberAppleSignInLauncher(
    onResult: (AppleSignInResult) -> Unit
): AppleSignInLauncher?
