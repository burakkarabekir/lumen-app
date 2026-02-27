package com.bksd.journal.domain.usecase

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.journal.domain.model.Moment
import com.bksd.journal.domain.repository.MomentRepository

import kotlinx.datetime.LocalDate

class GetMomentsByDateUseCase(private val repository: MomentRepository) {
    suspend operator fun invoke(date: LocalDate): Result<List<Moment>, AppError> {
        return repository.getMoments(date = date)
    }
}
