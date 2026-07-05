package com.bksd.core.presentation.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

typealias AppleSignInProvider =
        (onResult: (idToken: String?, nonce: String?, error: String?) -> Unit) -> Unit

object AppleSignInBridge {
    var provider: AppleSignInProvider? = null
}

@Composable
actual fun rememberAppleSignInLauncher(
    onResult: (AppleSignInResult) -> Unit
): AppleSignInLauncher? = remember {
    AppleSignInLauncher {
        val provider = AppleSignInBridge.provider
        if (provider == null) {
            onResult(AppleSignInResult.Failed("Apple sign-in is not available"))
        } else {
            provider { idToken, nonce, error ->
                when {
                    idToken != null -> onResult(AppleSignInResult.Success(idToken, nonce))
                    error == "cancelled" -> onResult(AppleSignInResult.Cancelled)
                    else -> onResult(AppleSignInResult.Failed(error ?: "Apple sign-in failed"))
                }
            }
        }
    }
}
