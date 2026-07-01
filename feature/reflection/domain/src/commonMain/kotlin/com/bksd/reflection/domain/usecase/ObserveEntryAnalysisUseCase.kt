package com.bksd.reflection.domain.usecase

import com.bksd.reflection.domain.model.MomentAnalysisState
import com.bksd.reflection.domain.repository.MomentAnalysisStore
import kotlinx.coroutines.flow.Flow

class ObserveEntryAnalysisUseCase(
    private val store: MomentAnalysisStore
) {
    operator fun invoke(momentId: String): Flow<MomentAnalysisState> = store.observe(momentId)
}
