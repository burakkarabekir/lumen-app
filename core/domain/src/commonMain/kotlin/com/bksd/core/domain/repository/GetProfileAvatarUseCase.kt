package com.bksd.core.domain.repository

import kotlinx.coroutines.flow.Flow

class GetProfileAvatarUseCase(
    private val repository: ProfileRepository
) {
    operator fun invoke(): Flow<String?> = repository.observeAvatarUrl()
}
