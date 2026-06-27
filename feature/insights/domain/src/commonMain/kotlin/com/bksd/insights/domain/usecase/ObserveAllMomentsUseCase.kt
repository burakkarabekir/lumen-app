package com.bksd.insights.domain.usecase

import com.bksd.core.domain.model.Moment
import com.bksd.core.domain.repository.MomentRepository
import kotlinx.coroutines.flow.Flow

class ObserveAllMomentsUseCase(private val repository: MomentRepository) {
    operator fun invoke(): Flow<List<Moment>> = repository.observeAllMoments()
}
