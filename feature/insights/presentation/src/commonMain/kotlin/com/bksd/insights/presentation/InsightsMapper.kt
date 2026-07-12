package com.bksd.insights.presentation

import com.bksd.insights.domain.model.InsightsMetrics
import com.bksd.insights.domain.model.InsightsRange
import com.bksd.insights.domain.model.StreakRun
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.getString

internal suspend fun InsightsMetrics.toUiState(selectedRange: InsightsRange): InsightsState {
    val rangeOptions = buildList {
        add(StatsRange.AllTime)
        years.forEach { add(StatsRange(it)) }
    }
    val axisLabels = if (barAxisMonthly) monthInitials() else barAxisLabels
    val classify = placeKindClassifier()
    return InsightsState(
        isLoading = false,
        hasActiveStreak = hasActiveStreak,
        currentStreak = CurrentStreak(
            value = currentWeekly?.length ?: 0,
            unit = StreakUnit.WEEK,
            since = currentWeekly?.start?.let(::formatDate).orEmpty(),
        ),
        currentDailyStreak = CurrentStreak(
            value = currentDaily?.length ?: 0,
            unit = StreakUnit.DAY,
            since = currentDaily?.start?.let(::formatDate).orEmpty(),
        ),
        longest = StreakDetail(
            kind = StreakKind.LONGEST,
            daily = longestDaily.toLine(StreakUnit.DAY, StreakAccent.CORAL),
            weekly = longestWeekly.toLine(StreakUnit.WEEK, StreakAccent.VIOLET),
        ),
        recent = StreakDetail(
            kind = StreakKind.RECENT,
            daily = recentDaily.toLine(StreakUnit.DAY, StreakAccent.NEUTRAL),
            weekly = recentWeekly.toLine(StreakUnit.WEEK, StreakAccent.NEUTRAL),
        ),
        entries = EntriesStat(
            total = entriesTotal,
            bars = bars.toPersistentList(),
            axisLabels = axisLabels.toPersistentList(),
            breakdown = listOf(
                StatItem(mediaMix.photos, EntryMediaKind.PHOTOS),
                StatItem(mediaMix.videos, EntryMediaKind.VIDEOS),
                StatItem(mediaMix.voiceNotes, EntryMediaKind.VOICE_NOTES),
                StatItem(mediaMix.places, EntryMediaKind.PLACES),
            ).toPersistentList(),
        ),
        places = places
            .map { VisitedPlace(it.name, it.count, classify(it.name)) }
            .toPersistentList(),
        writtenWords = writtenWords,
        journaled = JournaledStat(
            days = journaledDays,
            thisMonth = journaledThisMonth,
            thisYear = journaledThisYear,
        ),
        selectedRange = selectedRange.toUiClamped(years),
        rangeOptions = rangeOptions.toPersistentList(),
    )
}

internal fun StatsRange.toDomain(): InsightsRange =
    year?.let { InsightsRange.Year(it) } ?: InsightsRange.AllTime

private fun StreakRun?.toLine(unit: StreakUnit, accent: StreakAccent): StreakLine =
    if (this == null) {
        StreakLine(0, unit, "", "", accent)
    } else {
        StreakLine(
            count = length,
            unit = unit,
            startDate = formatDate(start),
            endDate = formatDate(end),
            accent = accent,
        )
    }

private suspend fun monthInitials(): List<String> = listOf(
    getString(Res.string.month_initial_jan),
    getString(Res.string.month_initial_feb),
    getString(Res.string.month_initial_mar),
    getString(Res.string.month_initial_apr),
    getString(Res.string.month_initial_may),
    getString(Res.string.month_initial_jun),
    getString(Res.string.month_initial_jul),
    getString(Res.string.month_initial_aug),
    getString(Res.string.month_initial_sep),
    getString(Res.string.month_initial_oct),
    getString(Res.string.month_initial_nov),
    getString(Res.string.month_initial_dec),
)

private fun InsightsRange.toUiClamped(years: List<Int>): StatsRange = when (this) {
    InsightsRange.AllTime -> StatsRange.AllTime
    is InsightsRange.Year -> if (year in years) StatsRange(year) else StatsRange.AllTime
}

private fun formatDate(date: LocalDate): String {
    val month = date.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
    return "$month ${date.day}, ${date.year}"
}
