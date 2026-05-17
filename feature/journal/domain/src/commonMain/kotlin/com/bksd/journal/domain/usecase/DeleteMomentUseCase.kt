package com.bksd.journal.domain.usecase

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.journal.domain.repository.MomentRepository

class DeleteMomentUseCase(
    private val repository: MomentRepository
) {
    suspend operator fun invoke(
        id: String
    ): Result<Unit, AppError> = repository.deleteMoment(id)
}
