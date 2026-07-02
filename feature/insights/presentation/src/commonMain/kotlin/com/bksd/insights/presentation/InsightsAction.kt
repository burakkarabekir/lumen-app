package com.bksd.insights.presentation

sealed interface InsightsAction {
    data object OnRefresh : InsightsAction
    data class OnStatsRangeSelect(val range: StatsRange) : InsightsAction
}
