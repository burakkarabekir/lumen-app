package com.bksd.profile.domain.usecase

import com.bksd.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class GetProfileAvatarUseCase(
    private val repository: ProfileRepository
) {
    operator fun invoke(): Flow<String?> = repository.observeAvatarUrl()
}
