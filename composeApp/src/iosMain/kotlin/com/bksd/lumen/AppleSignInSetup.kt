package com.bksd.lumen

import com.bksd.core.presentation.auth.AppleSignInBridge

fun setAppleSignInProvider(
    provider: (onResult: (idToken: String?, nonce: String?, error: String?) -> Unit) -> Unit
) {
    AppleSignInBridge.provider = provider
}
