package com.bksd.insights.presentation

import com.bksd.core.presentation.util.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel for the Emotional Insights screen.
 * Populates state with fake data for Phase 2 (UI-first development).
 */
class InsightsViewModel : BaseViewModel<InsightsAction, InsightsEvent>() {

    private val _stateFlow = MutableStateFlow(createFakeState())
    val state: StateFlow<InsightsState> = _stateFlow.asStateFlow()

    override fun onAction(action: InsightsAction) {
        when (action) {
            InsightsAction.OnRefresh -> {
                _stateFlow.value = createFakeState()
            }
        }
    }

    private fun createFakeState(): InsightsState = InsightsState(
        isLoading = false,
        peakActivityInsight = "Your insights are 24% deeper when recording after 9 PM.",
        consistencyTrend = ConsistencyTrend(
            title = "Journaling Gap",
            description = "You typically stop writing on Mondays when work stress peaks. Try a 1-min voice note instead."
        ),
        mediumBreakdown = MediumBreakdown(
            correlation = "+18%",
            metric = "Mood Lift via Voice",
            description = "Correlation"
        ),
        mindsetSynthesis = MindsetSynthesis(
            summary = "This week focused heavily on work stress but ended positively. You've successfully navigated a high-pressure deadline by increasing your evening reflection frequency.",
            recurringTheme = "Imposter Syndrome",
            adjustment = "Morning Gratitude",
            reflectionPrompt = "Reflection Prompt"
        )
    )
}
