package com.bksd.auth.domain.usecase

import com.bksd.auth.domain.AuthRepository
import com.bksd.core.domain.cleanup.LocalDataCleaner
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.storage.SessionStorage

class SignOutUseCase(
    private val repository: AuthRepository,
    private val localDataCleaners: List<LocalDataCleaner>,
    private val sessionStorage: SessionStorage,
) {
    suspend operator fun invoke(): Result<Unit, AppError> {
        val result = repository.signOut()
        if (result is Result.Success) {
            localDataCleaners.forEach { it.clearLocalData() }
            sessionStorage.setLocalDataOwner(null)
        }
        return result
    }
}
