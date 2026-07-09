package com.bksd.core.presentation.biometric

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

private const val AUTHENTICATORS =
    BiometricManager.Authenticators.BIOMETRIC_WEAK or
        BiometricManager.Authenticators.DEVICE_CREDENTIAL

private class AndroidBiometricAuthenticator(
    private val activity: FragmentActivity?,
) : BiometricAuthenticator {

    override fun isAvailable(): Boolean {
        val act = activity ?: return false
        return BiometricManager.from(act).canAuthenticate(AUTHENTICATORS) ==
            BiometricManager.BIOMETRIC_SUCCESS
    }

    override suspend fun authenticate(title: String, subtitle: String): BiometricResult {
        val act = activity ?: return BiometricResult.UNAVAILABLE
        return suspendCancellableCoroutine { continuation ->
            val prompt = BiometricPrompt(
                act,
                ContextCompat.getMainExecutor(act),
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        if (continuation.isActive) continuation.resume(BiometricResult.SUCCESS)
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        if (continuation.isActive) continuation.resume(BiometricResult.FAILED)
                    }
                },
            )
            val info = BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setAllowedAuthenticators(AUTHENTICATORS)
                .build()
            prompt.authenticate(info)
        }
    }
}

@Composable
actual fun rememberBiometricAuthenticator(): BiometricAuthenticator {
    val activity = LocalContext.current as? FragmentActivity
    return remember(activity) { AndroidBiometricAuthenticator(activity) }
}
