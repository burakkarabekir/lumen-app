package com.bksd.journal.domain.usecase

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.model.Moment
import com.bksd.core.domain.repository.MomentRepository

class GetMomentUseCase(
    private val repository: MomentRepository
) {
    suspend operator fun invoke(
        id: String
    ): Result<Moment, AppError> = repository.getMoment(id)
}
