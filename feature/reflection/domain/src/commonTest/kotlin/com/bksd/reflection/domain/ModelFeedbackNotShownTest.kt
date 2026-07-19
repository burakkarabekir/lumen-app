package com.bksd.reflection.domain

import com.bksd.core.domain.error.Result
import com.bksd.reflection.domain.model.DistressLevel
import com.bksd.reflection.domain.model.MomentReflection
import com.bksd.reflection.domain.support.SupportConfig
import com.bksd.reflection.domain.usecase.AnalyzeAndReflectUseCase
import com.bksd.reflection.domain.usecase.TrendSummaryBuilder
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals

class ModelFeedbackNotShownTest {

    private fun useCaseFor(distress: DistressLevel, feedback: String?) = AnalyzeAndReflectUseCase(
        reflector = FakeEntryReflector(Result.Success(reflectionResponse(distress, feedback))),
        store = FakeMomentAnalysisStore(),
        trendSummaryBuilder = TrendSummaryBuilder()
    )

    @Test
    fun elevatedShowsPrewrittenSupportNotModelWords() = runTest {
        val data = (useCaseFor(DistressLevel.ELEVATED, MODEL_WORDS)("m1", "x") as Result.Success).data
        assertIs<MomentReflection.Support>(data)
        assertEquals(SupportConfig.ELEVATED_MESSAGE, data.message)
        assertNotEquals(MODEL_WORDS, data.message)
    }

    @Test
    fun crisisShowsPrewrittenCrisisNotModelWords() = runTest {
        val data = (useCaseFor(DistressLevel.CRISIS, MODEL_WORDS)("m1", "x") as Result.Success).data
        assertIs<MomentReflection.Crisis>(data)
        assertEquals(SupportConfig.CRISIS_MESSAGE, data.message)
        assertNotEquals(MODEL_WORDS, data.message)
    }

    @Test
    fun lowDistressShowsModelFeedback() = runTest {
        val data = (useCaseFor(DistressLevel.NONE, "warm reflection")("m1", "x") as Result.Success).data
        assertIs<MomentReflection.Reflection>(data)
        assertEquals("warm reflection", data.message)
    }

    private companion object {
        const val MODEL_WORDS = "model-generated words that must never appear on a support card"
    }
}
