package com.bksd.insights.domain.usecase

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.repository.MomentRepository

class SyncAllMomentsUseCase(private val repository: MomentRepository) {
    suspend operator fun invoke(): Result<Unit, AppError> = repository.syncAllMoments()
}
