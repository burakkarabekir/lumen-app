package com.bksd.reflection.domain.usecase

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.reflection.domain.analysis.EntryReflector
import com.bksd.reflection.domain.analysis.toMomentReflection
import com.bksd.reflection.domain.model.MomentReflection
import com.bksd.reflection.domain.repository.MomentAnalysisStore

class AnalyzeAndReflectUseCase(
    private val reflector: EntryReflector,
    private val store: MomentAnalysisStore,
    private val trendSummaryBuilder: TrendSummaryBuilder,
) {
    suspend operator fun invoke(
        momentId: String,
        entryText: String,
        moods: List<String> = emptyList()
    ): Result<MomentReflection, AppError> {
        val trend = trendSummaryBuilder.build(store.recentAnalyses(TREND_WINDOW))

        val response = when (val result = reflector.reflect(momentId, entryText, moods, trend)) {
            is Result.Success -> result.data
            is Result.Error -> return Result.Error(result.error)
        }
        return Result.Success(response.toMomentReflection())
    }

    private companion object {
        const val TREND_WINDOW = 7
    }
}
