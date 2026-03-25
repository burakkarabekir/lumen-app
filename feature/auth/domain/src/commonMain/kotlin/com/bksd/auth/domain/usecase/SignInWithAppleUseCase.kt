package com.bksd.auth.domain.usecase

import com.bksd.auth.domain.AuthRepository
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result

class SignInWithAppleUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(platformContext: Any?): Result<Unit, AppError> =
        repository.signInWithApple(platformContext)
}
