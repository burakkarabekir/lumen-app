package com.bksd.core.presentation.auth

import androidx.compose.runtime.Composable

sealed interface GoogleSignInResult {
    data class Success(val idToken: String) : GoogleSignInResult
    data object Cancelled : GoogleSignInResult
    data class Failed(val message: String) : GoogleSignInResult
}

fun interface GoogleSignInLauncher {
    fun launch()
}

@Composable
expect fun rememberGoogleSignInLauncher(
    onResult: (GoogleSignInResult) -> Unit
): GoogleSignInLauncher
