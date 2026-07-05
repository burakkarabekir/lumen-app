package com.bksd.core.presentation.biometric

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.LocalAuthentication.LAContext
import platform.LocalAuthentication.LAPolicyDeviceOwnerAuthentication
import kotlin.coroutines.resume

private class IosBiometricAuthenticator : BiometricAuthenticator {

    @OptIn(ExperimentalForeignApi::class)
    override fun isAvailable(): Boolean =
        LAContext().canEvaluatePolicy(LAPolicyDeviceOwnerAuthentication, null)

    override suspend fun authenticate(title: String, subtitle: String): BiometricResult =
        suspendCancellableCoroutine { continuation ->
            LAContext().evaluatePolicy(
                policy = LAPolicyDeviceOwnerAuthentication,
                localizedReason = subtitle,
            ) { success, _ ->
                if (continuation.isActive) {
                    continuation.resume(
                        if (success) BiometricResult.SUCCESS else BiometricResult.FAILED
                    )
                }
            }
        }
}

@Composable
actual fun rememberBiometricAuthenticator(): BiometricAuthenticator =
    remember { IosBiometricAuthenticator() }
