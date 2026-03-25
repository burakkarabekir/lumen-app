package com.bksd.core.data.auth

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result

interface SocialAuthProvider {
    suspend fun signInWithGoogle(platformContext: Any?): Result<Unit, AppError>
    suspend fun signInWithApple(platformContext: Any?): Result<Unit, AppError>
}
