package com.bksd.journal.domain.usecase

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.repository.MomentRepository

class SyncMomentsUseCase(private val repository: MomentRepository) {
    suspend operator fun invoke(limit: Int, offset: Int): Result<Unit, AppError> {
        return repository.syncMomentsPaged(limit = limit, offset = offset)
    }
}
