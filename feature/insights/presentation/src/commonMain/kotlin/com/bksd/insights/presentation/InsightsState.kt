package com.bksd.insights.presentation

import androidx.compose.runtime.Immutable

/**
 * Immutable UI state for the Emotional Insights screen.
 * All types are stable for Compose recomposition safety.
 */
@Immutable
data class InsightsState(
    val isLoading: Boolean = false,
    val peakActivityInsight: String = "",
    val consistencyTrend: ConsistencyTrend? = null,
    val mediumBreakdown: MediumBreakdown? = null,
    val mindsetSynthesis: MindsetSynthesis? = null
)

data class ConsistencyTrend(
    val title: String,
    val description: String
)

data class MediumBreakdown(
    val correlation: String,
    val metric: String,
    val description: String
)

data class MindsetSynthesis(
    val summary: String,
    val recurringTheme: String,
    val adjustment: String,
    val reflectionPrompt: String
)
