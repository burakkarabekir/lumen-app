package com.bksd.profile.domain.usecase

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.profile.domain.model.UserProfile
import com.bksd.profile.domain.repository.ProfileRepository

class GetUserProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): Result<UserProfile, AppError> = repository.getUserProfile()
}
