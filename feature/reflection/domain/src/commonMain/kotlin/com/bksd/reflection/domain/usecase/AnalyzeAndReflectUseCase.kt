package com.bksd.reflection.domain.usecase

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.reflection.domain.analysis.EntryReflector
import com.bksd.reflection.domain.model.DistressLevel
import com.bksd.reflection.domain.model.EntryAnalysis
import com.bksd.reflection.domain.model.MomentReflection
import com.bksd.reflection.domain.repository.MomentAnalysisStore
import com.bksd.reflection.domain.support.SupportConfig

class AnalyzeAndReflectUseCase(
    private val reflector: EntryReflector,
    private val store: MomentAnalysisStore,
    private val trendSummaryBuilder: TrendSummaryBuilder,
) {
    suspend operator fun invoke(
        entryText: String,
        mood: String? = null
    ): Result<MomentReflection, AppError> {
        val trend = trendSummaryBuilder.build(store.recentAnalyses(TREND_WINDOW))

        val response = when (val result = reflector.reflect(entryText, mood, trend)) {
            is Result.Success -> result.data
            is Result.Error -> return Result.Error(result.error)
        }

        val analysis = response.analysis
        val coverImageUrl = response.coverImageUrl
        val reflection = when (analysis.distress) {
            DistressLevel.NONE, DistressLevel.MILD ->
                MomentReflection.Reflection(
                    analysis = analysis,
                    message = response.feedback ?: GENTLE_FALLBACK,
                    question = response.question,
                    coverImageUrl = coverImageUrl
                )

            DistressLevel.ELEVATED -> elevatedSupport(analysis, coverImageUrl)
            DistressLevel.CRISIS -> crisisSupport(analysis, coverImageUrl)
        }
        return Result.Success(reflection)
    }

    private fun elevatedSupport(analysis: EntryAnalysis, coverImageUrl: String?): MomentReflection.Support =
        MomentReflection.Support(
            analysis = analysis,
            message = SupportConfig.ELEVATED_MESSAGE,
            mentalHealthLines = SupportConfig.mentalHealthLines,
            coverImageUrl = coverImageUrl
        )

    private fun crisisSupport(analysis: EntryAnalysis, coverImageUrl: String?): MomentReflection.Crisis =
        MomentReflection.Crisis(
            analysis = analysis,
            message = SupportConfig.CRISIS_MESSAGE,
            emergency = SupportConfig.emergency,
            crisisLines = SupportConfig.crisisLines,
            coverImageUrl = coverImageUrl
        )

    private companion object {
        const val TREND_WINDOW = 7
        const val GENTLE_FALLBACK = "Thanks for taking a moment to write this down."
    }
}
