package com.bksd.insights.presentation

import com.bksd.core.presentation.util.UiText

/**
 * One-shot side-effects for the Emotional Insights screen.
 */
sealed interface InsightsEvent {
    data class ShowError(val message: UiText) : InsightsEvent
}
