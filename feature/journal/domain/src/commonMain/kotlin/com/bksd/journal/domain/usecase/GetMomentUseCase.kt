package com.bksd.journal.domain.usecase

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.journal.domain.model.Moment
import com.bksd.journal.domain.repository.MomentRepository

class GetMomentUseCase(private val repository: MomentRepository) {
    suspend operator fun invoke(id: String): Result<Moment, AppError> {
        return repository.getMoment(id)
    }
}
