package com.bksd.profile.domain.usecase

import com.bksd.profile.domain.repository.ProfileRepository

class SetProfileAvatarUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(bytes: ByteArray, mimeType: String?) {
        val path = repository.saveAvatarImage(bytes, mimeType)
        repository.setAvatarUrl(path)
    }
}
