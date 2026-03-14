package com.bksd.profile.domain.usecase

import com.bksd.profile.domain.repository.ProfileRepository

class ClearUserDataUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke() {
        repository.clearUserData()
    }
}
