package com.bksd.core.presentation.auth

import androidx.compose.runtime.Composable

@Composable
actual fun rememberAppleSignInLauncher(
    onResult: (AppleSignInResult) -> Unit
): AppleSignInLauncher? = null
