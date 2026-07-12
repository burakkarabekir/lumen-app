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
    val longest: StreakDetail = StreakDetail(StreakKind.LONGEST),
    val recent: StreakDetail = StreakDetail(StreakKind.RECENT),
    val entries: EntriesStat = EntriesStat(),
    val places: ImmutableList<VisitedPlace> = persistentListOf(),
    val writtenWords: Int = 0,
    val journaled: JournaledStat = JournaledStat(),
    val selectedRange: StatsRange = StatsRange.AllTime,
    val rangeOptions: ImmutableList<StatsRange> = persistentListOf(StatsRange.AllTime),
)

enum class StreakUnit { DAY, WEEK }

enum class StreakKind { LONGEST, RECENT }

enum class EntryMediaKind { PHOTOS, VIDEOS, VOICE_NOTES, PLACES }

@Immutable
data class CurrentStreak(
    val value: Int = 0,
    val unit: StreakUnit = StreakUnit.WEEK,
    val since: String = "",
)

@Immutable
data class StreakDetail(
    val kind: StreakKind,
    val daily: StreakLine = StreakLine(unit = StreakUnit.DAY),
    val weekly: StreakLine = StreakLine(unit = StreakUnit.WEEK),
)

@Immutable
data class StreakLine(
    val count: Int = 0,
    val unit: StreakUnit = StreakUnit.DAY,
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
data class StatItem(val value: Int, val kind: EntryMediaKind)

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
data class StatsRange(val year: Int?) {
    companion object {
        val AllTime = StatsRange(null)
    }
}
