package com.bksd.profile.domain.usecase

import com.bksd.profile.domain.repository.ProfileRepository

class SetProfileAvatarUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(bytes: ByteArray, mimeType: String?): String {
        return repository.uploadAvatar(bytes, mimeType)
    }
}
