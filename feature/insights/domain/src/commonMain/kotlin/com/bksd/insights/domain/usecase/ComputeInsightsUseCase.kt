package com.bksd.insights.domain.usecase

import com.bksd.core.domain.model.Moment
import com.bksd.insights.domain.calculator.InsightsCalculator
import com.bksd.insights.domain.model.InsightsMetrics
import com.bksd.insights.domain.model.InsightsRange

class ComputeInsightsUseCase(private val calculator: InsightsCalculator) {
    operator fun invoke(moments: List<Moment>, range: InsightsRange): InsightsMetrics =
        calculator.compute(moments, range)
}
