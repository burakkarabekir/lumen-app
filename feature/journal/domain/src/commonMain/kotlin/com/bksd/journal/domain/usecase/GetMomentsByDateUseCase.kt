package com.bksd.journal.domain.usecase

import com.bksd.journal.domain.model.Moment
import com.bksd.journal.domain.repository.MomentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

class GetMomentsByDateUseCase(private val repository: MomentRepository) {
    operator fun invoke(date: LocalDate): Flow<List<Moment>> {
        return repository.observeMoments(date = date)
    }
}
