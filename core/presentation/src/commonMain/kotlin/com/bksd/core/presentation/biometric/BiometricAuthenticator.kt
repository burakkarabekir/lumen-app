package com.bksd.core.presentation.biometric

import androidx.compose.runtime.Composable

enum class BiometricResult {
    SUCCESS,
    FAILED,
    UNAVAILABLE,
}

interface BiometricAuthenticator {
    fun isAvailable(): Boolean
    suspend fun authenticate(title: String, subtitle: String): BiometricResult
}

@Composable
expect fun rememberBiometricAuthenticator(): BiometricAuthenticator
