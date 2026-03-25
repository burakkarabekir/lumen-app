package com.bksd.profile.domain.usecase

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.profile.domain.repository.ProfileRepository

class SetProfileAvatarUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(bytes: ByteArray, mimeType: String?): Result<String, AppError> {
        return repository.uploadAvatar(bytes, mimeType)
    }
}
