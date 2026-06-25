package com.bksd.lumen

import com.bksd.core.presentation.auth.GoogleSignInBridge

fun setGoogleSignInProvider(
    provider: (onResult: (idToken: String?, error: String?) -> Unit) -> Unit
) {
    GoogleSignInBridge.provider = provider
}
