package com.bksd.reflection.domain.usecase

import com.bksd.core.domain.error.Result
import com.bksd.reflection.domain.repository.MomentAnalysisStore

class RequestEntryAnalysisUseCase(
    private val analyzeAndReflect: AnalyzeAndReflectUseCase,
    private val store: MomentAnalysisStore,
) {
    suspend operator fun invoke(momentId: String, entryText: String, mood: String?) {
        store.setPending(momentId)
        when (val result = analyzeAndReflect(entryText, mood)) {
            is Result.Success -> store.setResult(momentId, result.data)
            is Result.Error -> store.setFailed(momentId)
        }
    }
}
