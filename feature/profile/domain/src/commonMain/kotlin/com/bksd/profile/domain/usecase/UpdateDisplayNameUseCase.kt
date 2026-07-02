package com.bksd.profile.domain.usecase

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.profile.domain.repository.ProfileRepository

class UpdateDisplayNameUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(name: String): Result<Unit, AppError> {
        return repository.updateDisplayName(name.trim())
    }
}
