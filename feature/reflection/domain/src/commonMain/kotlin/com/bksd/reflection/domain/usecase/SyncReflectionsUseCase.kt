package com.bksd.reflection.domain.usecase

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.reflection.domain.analysis.EntryReflectionSource
import com.bksd.reflection.domain.repository.MomentAnalysisStore

class SyncReflectionsUseCase(
    private val source: EntryReflectionSource,
    private val store: MomentAnalysisStore,
) {
    suspend operator fun invoke(): Result<Unit, AppError> {
        return when (val result = source.fetchAll()) {
            is Result.Success -> {
                result.data.forEach { store.setResult(it.momentId, it.reflection) }
                Result.Success(Unit)
            }

            is Result.Error -> Result.Error(result.error)
        }
    }
}
