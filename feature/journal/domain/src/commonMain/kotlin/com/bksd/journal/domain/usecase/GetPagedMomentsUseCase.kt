package com.bksd.journal.domain.usecase

import com.bksd.journal.domain.model.Moment
import com.bksd.journal.domain.repository.MomentRepository
import kotlinx.coroutines.flow.Flow

class GetPagedMomentsUseCase(private val repository: MomentRepository) {
    operator fun invoke(limit: Int, offset: Int): Flow<List<Moment>> =
        repository.observeMomentsPaged(limit, offset)
}
