package com.bksd.insights.presentation

import com.bksd.insights.domain.model.InsightsMetrics
import com.bksd.insights.domain.model.InsightsPlaceKind
import com.bksd.insights.domain.model.InsightsRange
import com.bksd.insights.domain.model.PlaceCount
import com.bksd.insights.domain.model.StreakRun
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.LocalDate

internal fun InsightsMetrics.toUiState(selectedRange: InsightsRange): InsightsState {
    val rangeOptions = buildList {
        add(StatsRange.AllTime)
        years.forEach { add(StatsRange(it.toString(), it)) }
    }
    return InsightsState(
        isLoading = false,
        hasActiveStreak = hasActiveStreak,
        currentStreak = CurrentStreak(
            value = currentWeekly?.length ?: 0,
            unit = plural(currentWeekly?.length ?: 0, "Week"),
            since = currentWeekly?.start?.let(::formatDate).orEmpty(),
        ),
        longest = StreakDetail(
            title = "Longest",
            daily = longestDaily.toLine("Day", StreakAccent.CORAL),
            weekly = longestWeekly.toLine("Week", StreakAccent.VIOLET),
        ),
        recent = StreakDetail(
            title = "Recent",
            daily = recentDaily.toLine("Day", StreakAccent.NEUTRAL),
            weekly = recentWeekly.toLine("Week", StreakAccent.NEUTRAL),
        ),
        entries = EntriesStat(
            total = entriesTotal,
            bars = bars.toPersistentList(),
            axisLabels = barAxisLabels.toPersistentList(),
            breakdown = listOf(
                StatItem(mediaMix.photos, "Photos"),
                StatItem(mediaMix.videos, "Videos"),
                StatItem(mediaMix.voiceNotes, "Voice Notes"),
                StatItem(mediaMix.places, "Places"),
            ).toPersistentList(),
        ),
        places = places.map { it.toUi() }.toPersistentList(),
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

private fun StreakRun?.toLine(unitSingular: String, accent: StreakAccent): StreakLine =
    if (this == null) {
        StreakLine(0, plural(0, unitSingular), "", "", accent)
    } else {
        StreakLine(
            count = length,
            unit = plural(length, unitSingular),
            startDate = formatDate(start),
            endDate = formatDate(end),
            accent = accent,
        )
    }

private fun PlaceCount.toUi(): VisitedPlace = VisitedPlace(name, count, kind.toUi())

private fun InsightsPlaceKind.toUi(): PlaceKind = when (this) {
    InsightsPlaceKind.BEACH -> PlaceKind.BEACH
    InsightsPlaceKind.LANDMARK -> PlaceKind.LANDMARK
    InsightsPlaceKind.PARK -> PlaceKind.PARK
    InsightsPlaceKind.RESTAURANT -> PlaceKind.RESTAURANT
    InsightsPlaceKind.GENERIC -> PlaceKind.GENERIC
}

private fun InsightsRange.toUiClamped(years: List<Int>): StatsRange = when (this) {
    InsightsRange.AllTime -> StatsRange.AllTime
    is InsightsRange.Year -> if (year in years) StatsRange(year.toString(), year) else StatsRange.AllTime
}

private fun plural(count: Int, singular: String): String =
    if (count == 1) singular else "${singular}s"

private fun formatDate(date: LocalDate): String {
    val month = date.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
    return "$month ${date.day}, ${date.year}"
}
