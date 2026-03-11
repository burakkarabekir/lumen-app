package com.bksd.core.domain.repository

class SetProfileAvatarUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(bytes: ByteArray, mimeType: String?) {
        val path = repository.saveAvatarImage(bytes, mimeType)
        repository.setAvatarUrl(path)
    }
}
