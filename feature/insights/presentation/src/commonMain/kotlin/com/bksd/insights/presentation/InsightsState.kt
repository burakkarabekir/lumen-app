package com.bksd.insights.presentation

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class InsightsState(
    val isLoading: Boolean = false,
    val hasActiveStreak: Boolean = false,
    val currentStreak: CurrentStreak = CurrentStreak(),
    val currentDailyStreak: CurrentStreak = CurrentStreak(),
    val longest: StreakDetail = StreakDetail("Longest"),
    val recent: StreakDetail = StreakDetail("Recent"),
    val entries: EntriesStat = EntriesStat(),
    val places: ImmutableList<VisitedPlace> = persistentListOf(),
    val writtenWords: Int = 0,
    val journaled: JournaledStat = JournaledStat(),
    val selectedRange: StatsRange = StatsRange.AllTime,
    val rangeOptions: ImmutableList<StatsRange> = persistentListOf(StatsRange.AllTime),
)

@Immutable
data class CurrentStreak(
    val value: Int = 0,
    val unit: String = "Weeks",
    val since: String = "",
)

@Immutable
data class StreakDetail(
    val title: String,
    val daily: StreakLine = StreakLine(unit = "Days"),
    val weekly: StreakLine = StreakLine(unit = "Weeks"),
)

@Immutable
data class StreakLine(
    val count: Int = 0,
    val unit: String = "Days",
    val startDate: String = "",
    val endDate: String = "",
    val accent: StreakAccent = StreakAccent.NEUTRAL,
)

enum class StreakAccent { CORAL, VIOLET, NEUTRAL }

@Immutable
data class EntriesStat(
    val total: Int = 0,
    val bars: ImmutableList<Int> = persistentListOf(),
    val axisLabels: ImmutableList<String> = persistentListOf(),
    val breakdown: ImmutableList<StatItem> = persistentListOf(),
)

@Immutable
data class StatItem(val value: Int, val label: String)

@Immutable
data class VisitedPlace(val name: String, val count: Int, val kind: PlaceKind)

enum class PlaceKind { BEACH, LANDMARK, PARK, RESTAURANT, GENERIC }

@Immutable
data class JournaledStat(
    val days: Int = 0,
    val thisMonth: Int = 0,
    val thisYear: Int = 0,
)

@Immutable
data class StatsRange(val label: String, val year: Int?) {
    companion object {
        val AllTime = StatsRange("All-time", null)
    }
}
