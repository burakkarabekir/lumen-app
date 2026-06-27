package com.bksd.insights.domain.model

import kotlinx.datetime.LocalDate

data class InsightsMetrics(
    val hasActiveStreak: Boolean,
    val currentWeekly: StreakRun?,
    val longestDaily: StreakRun?,
    val longestWeekly: StreakRun?,
    val recentDaily: StreakRun?,
    val recentWeekly: StreakRun?,
    val entriesTotal: Int,
    val bars: List<Int>,
    val barAxisLabels: List<String>,
    val mediaMix: MediaMix,
    val writtenWords: Int,
    val journaledDays: Int,
    val journaledThisMonth: Int,
    val journaledThisYear: Int,
    val places: List<PlaceCount>,
    val years: List<Int>,
)

data class StreakRun(
    val length: Int,
    val start: LocalDate,
    val end: LocalDate,
)

data class MediaMix(
    val photos: Int,
    val videos: Int,
    val voiceNotes: Int,
    val places: Int,
)

data class PlaceCount(
    val name: String,
    val count: Int,
    val kind: InsightsPlaceKind,
)

enum class InsightsPlaceKind { BEACH, LANDMARK, PARK, RESTAURANT, GENERIC }

sealed interface InsightsRange {
    data object AllTime : InsightsRange
    data class Year(val year: Int) : InsightsRange
}
