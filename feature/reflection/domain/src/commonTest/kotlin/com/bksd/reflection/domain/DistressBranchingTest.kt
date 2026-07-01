package com.bksd.reflection.domain

import com.bksd.core.domain.error.Result
import com.bksd.reflection.domain.model.DistressLevel
import com.bksd.reflection.domain.model.MomentReflection
import com.bksd.reflection.domain.usecase.AnalyzeAndReflectUseCase
import com.bksd.reflection.domain.usecase.TrendSummaryBuilder
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertIs

class DistressBranchingTest {

    private fun useCaseFor(distress: DistressLevel) = AnalyzeAndReflectUseCase(
        reflector = FakeEntryReflector(Result.Success(reflectionResponse(distress, "a gentle reflection"))),
        store = FakeMomentAnalysisStore(),
        trendSummaryBuilder = TrendSummaryBuilder()
    )

    @Test
    fun noneBranchesToReflection() = runTest {
        val data = (useCaseFor(DistressLevel.NONE)("a fine day") as Result.Success).data
        assertIs<MomentReflection.Reflection>(data)
    }

    @Test
    fun mildBranchesToReflection() = runTest {
        val data = (useCaseFor(DistressLevel.MILD)("a bit tired") as Result.Success).data
        assertIs<MomentReflection.Reflection>(data)
    }

    @Test
    fun elevatedBranchesToSupport() = runTest {
        val data = (useCaseFor(DistressLevel.ELEVATED)("struggling") as Result.Success).data
        assertIs<MomentReflection.Support>(data)
    }

    @Test
    fun crisisBranchesToCrisis() = runTest {
        val data = (useCaseFor(DistressLevel.CRISIS)("unsafe") as Result.Success).data
        assertIs<MomentReflection.Crisis>(data)
    }
}
