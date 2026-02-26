package com.bksd.moment.domain.usecase

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.journal.domain.model.Moment
import com.bksd.journal.domain.repository.MomentRepository

class SaveMomentUseCase(
    private val repository: MomentRepository
) {
    suspend operator fun invoke(moment: Moment): Result<Unit, AppError> {
        return repository.saveMoment(moment)
    }
}
