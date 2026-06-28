package com.bksd.profile.domain.usecase

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.profile.domain.model.UserProfile
import com.bksd.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class ObserveUserProfileUseCase(
    private val repository: ProfileRepository
) {
    operator fun invoke(): Flow<Result<UserProfile, AppError>> = repository.observeUserProfile()
}
