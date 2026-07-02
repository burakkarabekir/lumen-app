package com.bksd.auth.domain.usecase

import com.bksd.auth.domain.AuthRepository
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result

class GoogleSignInUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(idToken: String): Result<Unit, AppError> =
        authRepository.signInWithGoogle(idToken)
}
