package com.bksd.journal.domain.usecase

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.journal.domain.model.Moment
import com.bksd.journal.domain.repository.MomentRepository

class UpdateMomentUseCase(
    private val repository: MomentRepository
) {
    suspend operator fun invoke(
        moment: Moment
    ): Result<Unit, AppError> = repository.saveMoment(moment)
}
