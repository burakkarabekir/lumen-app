package com.bksd.reflection.domain

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.reflection.domain.analysis.EntryReflector
import com.bksd.reflection.domain.analysis.ReflectionResponse
import com.bksd.reflection.domain.model.DistressLevel
import com.bksd.reflection.domain.model.EntryAnalysis
import com.bksd.reflection.domain.model.MomentAnalysisState
import com.bksd.reflection.domain.model.MomentReflection
import com.bksd.reflection.domain.model.MoodValence
import com.bksd.reflection.domain.repository.MomentAnalysisStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeEntryReflector(
    private val response: Result<ReflectionResponse, AppError>
) : EntryReflector {
    var lastTrend: String? = null
        private set

    override suspend fun reflect(
        entryText: String,
        mood: String?,
        trend: String?
    ): Result<ReflectionResponse, AppError> {
        lastTrend = trend
        return response
    }
}

class FakeMomentAnalysisStore : MomentAnalysisStore {
    val results = mutableMapOf<String, MomentReflection>()
    var deleteAllCount: Int = 0
        private set

    override fun observe(momentId: String): Flow<MomentAnalysisState> =
        flowOf(MomentAnalysisState.None)

    override suspend fun setPending(momentId: String) = Unit
    override suspend fun setResult(momentId: String, reflection: MomentReflection) {
        results[momentId] = reflection
    }

    override suspend fun setFailed(momentId: String, quotaExceeded: Boolean) = Unit
    override suspend fun setOffline(momentId: String) = Unit
    override suspend fun recentAnalyses(limit: Int): List<EntryAnalysis> = emptyList()
    override suspend fun deleteAll() {
        deleteAllCount++
        results.clear()
    }
}

fun analysisWith(distress: DistressLevel): EntryAnalysis = EntryAnalysis(
    summary = "A factual one-line summary.",
    moodValence = MoodValence.NEUTRAL,
    moodConfidence = 0.2,
    dominantEmotions = emptyList(),
    themes = listOf("routine"),
    distress = distress,
    distressRationale = "internal"
)

fun reflectionResponse(distress: DistressLevel, feedback: String?): ReflectionResponse =
    ReflectionResponse(analysis = analysisWith(distress), feedback = feedback, question = null)
