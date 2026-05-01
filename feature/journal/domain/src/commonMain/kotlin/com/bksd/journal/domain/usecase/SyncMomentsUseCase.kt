package com.bksd.journal.domain.usecase

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.journal.domain.repository.MomentRepository
import kotlinx.datetime.LocalDate

class SyncMomentsUseCase(private val repository: MomentRepository) {
    suspend operator fun invoke(date: LocalDate): Result<Unit, AppError> {
        return repository.syncMoments(date = date)
    }
}
