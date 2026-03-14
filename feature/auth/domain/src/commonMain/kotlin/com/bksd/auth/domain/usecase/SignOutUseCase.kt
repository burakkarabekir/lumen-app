package com.bksd.auth.domain.usecase

import com.bksd.auth.domain.AuthRepository
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result

class SignOutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Result<Unit, AppError> {
        return repository.signOut()
    }
}
