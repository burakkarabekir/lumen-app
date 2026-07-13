package com.bksd.onboarding.domain.repository

import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {
    fun observeCompleted(): Flow<Boolean>
    suspend fun setCompleted()
}
