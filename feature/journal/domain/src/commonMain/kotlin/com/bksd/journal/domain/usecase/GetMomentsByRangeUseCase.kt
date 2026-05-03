package com.bksd.journal.domain.usecase

import com.bksd.journal.domain.model.Moment
import com.bksd.journal.domain.repository.MomentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

class GetMomentsByRangeUseCase(private val repository: MomentRepository) {
    operator fun invoke(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<Moment>> = repository.observeMomentsByRange(startDate, endDate)
}
