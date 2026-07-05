package com.bksd.auth.domain.usecase

import com.bksd.auth.domain.AuthRepository
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result

class AppleSignInUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(idToken: String, nonce: String?): Result<Unit, AppError> =
        authRepository.signInWithApple(idToken, nonce)
}
