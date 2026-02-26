package com.bksd.insights.presentation

/**
 * User intents for the Emotional Insights screen.
 * Currently read-only; actions will be added when interactivity is needed.
 */
sealed interface InsightsAction {
    data object OnRefresh : InsightsAction
}
