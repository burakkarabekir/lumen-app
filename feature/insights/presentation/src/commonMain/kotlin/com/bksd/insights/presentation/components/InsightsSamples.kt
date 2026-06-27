package com.bksd.insights.presentation.components

import com.bksd.insights.presentation.CurrentStreak
import com.bksd.insights.presentation.EntriesStat
import com.bksd.insights.presentation.InsightsState
import com.bksd.insights.presentation.JournaledStat
import com.bksd.insights.presentation.PlaceKind
import com.bksd.insights.presentation.StatItem
import com.bksd.insights.presentation.StatsRange
import com.bksd.insights.presentation.StreakAccent
import com.bksd.insights.presentation.StreakDetail
import com.bksd.insights.presentation.StreakLine
import com.bksd.insights.presentation.VisitedPlace
import kotlinx.collections.immutable.persistentListOf

internal val SampleCurrentStreak = CurrentStreak(15, "Weeks", "Dec 21, 2024")

internal val SampleLongest = StreakDetail(
    title = "Longest",
    daily = StreakLine(7, "Days", "May 24, 2024", "May 31, 2024", StreakAccent.CORAL),
    weekly = StreakLine(15, "Weeks", "Dec 21, 2024", "Apr 1, 2025", StreakAccent.VIOLET),
)

internal val SampleRecent = StreakDetail(
    title = "Recent",
    daily = StreakLine(5, "Days", "Mar 22, 2025", "Mar 26, 2025", StreakAccent.NEUTRAL),
    weekly = StreakLine(11, "Weeks", "Jul 2, 2024", "Sep 10, 2024", StreakAccent.NEUTRAL),
)

internal val SampleEntries = EntriesStat(
    total = 83,
    bars = persistentListOf(12, 16, 11, 18, 20, 15, 24, 19, 16, 28, 22, 18, 30, 44, 56, 34),
    axisLabels = persistentListOf("2024", "2025"),
    breakdown = persistentListOf(
        StatItem(51, "Photos"),
        StatItem(7, "Videos"),
        StatItem(4, "Voice Notes"),
        StatItem(18, "Places"),
    ),
)

internal val SamplePlaces = persistentListOf(
    VisitedPlace("California", 4, PlaceKind.GENERIC),
    VisitedPlace("Beach", 3, PlaceKind.BEACH),
    VisitedPlace("Park", 2, PlaceKind.PARK),
    VisitedPlace("Restaurant", 2, PlaceKind.RESTAURANT),
)

internal val SampleJournaled = JournaledStat(82, 1, 34)

internal val SampleInsightsState = InsightsState(
    hasActiveStreak = true,
    currentStreak = SampleCurrentStreak,
    longest = SampleLongest,
    recent = SampleRecent,
    entries = SampleEntries,
    places = SamplePlaces,
    writtenWords = 4930,
    journaled = SampleJournaled,
    selectedRange = StatsRange.AllTime,
    rangeOptions = persistentListOf(
        StatsRange.AllTime,
        StatsRange("2025", 2025),
        StatsRange("2024", 2024),
    ),
)
