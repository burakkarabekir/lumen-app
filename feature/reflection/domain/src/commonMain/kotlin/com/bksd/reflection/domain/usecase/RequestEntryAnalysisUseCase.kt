package com.bksd.reflection.domain.usecase

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.NetworkErrorType
import com.bksd.core.domain.error.QuotaErrorType
import com.bksd.core.domain.error.Result
import com.bksd.reflection.domain.model.QuotaLimit
import com.bksd.reflection.domain.repository.MomentAnalysisStore

class RequestEntryAnalysisUseCase(
    private val analyzeAndReflect: AnalyzeAndReflectUseCase,
    private val store: MomentAnalysisStore,
) {
    suspend operator fun invoke(momentId: String, entryText: String, mood: String?) {
        store.setPending(momentId)
        when (val result = analyzeAndReflect(entryText, mood)) {
            is Result.Success -> store.setResult(momentId, result.data)
            is Result.Error -> {
                when (val err = result.error) {
                    is AppError.Quota -> when (err.type) {
                        QuotaErrorType.DAILY_LIMIT -> store.setQuotaExceeded(momentId, QuotaLimit.DAILY)
                        QuotaErrorType.FREE_LIMIT,
                        QuotaErrorType.PREMIUM_REQUIRED -> store.setQuotaExceeded(momentId, QuotaLimit.FREE)
                        QuotaErrorType.CHECK_FAILED -> store.setFailed(momentId)
                    }

                    is AppError.Network -> when (err.type) {
                        NetworkErrorType.NO_INTERNET -> store.setOffline(momentId)
                        else -> store.setFailed(momentId)
                    }

                    else -> store.setFailed(momentId)
                }
            }
        }
    }
}
