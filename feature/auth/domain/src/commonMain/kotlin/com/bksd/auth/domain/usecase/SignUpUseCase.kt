package com.bksd.auth.domain.usecase

import com.bksd.auth.domain.AuthRepository
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result

class SignUpUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(
        email: String,
        password: String,
        fullName: String
    ): Result<Unit, AppError> {
        return repository.signUp(email = email, password = password, fullName = fullName)
    }
}
