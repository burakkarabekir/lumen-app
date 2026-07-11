package com.bksd.auth.domain.usecase

import com.bksd.auth.domain.AuthRepository
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.storage.SessionStorage

class SignUpUseCase(
    private val repository: AuthRepository,
    private val sessionStorage: SessionStorage,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        fullName: String
    ): Result<Unit, AppError> {
        return repository.signUp(email = email, password = password, fullName = fullName)
            .also { if (it is Result.Success) sessionStorage.setFirstLoginPending() }
    }
}
