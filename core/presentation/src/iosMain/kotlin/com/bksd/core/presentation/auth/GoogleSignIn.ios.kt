package com.bksd.core.presentation.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

typealias GoogleSignInProvider =
        (onResult: (idToken: String?, error: String?) -> Unit) -> Unit

object GoogleSignInBridge {
    var provider: GoogleSignInProvider? = null
}

@Composable
actual fun rememberGoogleSignInLauncher(
    onResult: (GoogleSignInResult) -> Unit
): GoogleSignInLauncher = remember {
    GoogleSignInLauncher {
        val provider = GoogleSignInBridge.provider
        if (provider == null) {
            onResult(GoogleSignInResult.Failed("Google sign-in is not available"))
        } else {
            provider { idToken, error ->
                when {
                    idToken != null -> onResult(GoogleSignInResult.Success(idToken))
                    error == "cancelled" -> onResult(GoogleSignInResult.Cancelled)
                    else -> onResult(GoogleSignInResult.Failed(error ?: "Google sign-in failed"))
                }
            }
        }
    }
}
