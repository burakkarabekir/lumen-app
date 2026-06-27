package com.bksd.insights.domain.calculator

import com.bksd.core.domain.model.AudioAttachment
import com.bksd.core.domain.model.Moment
import com.bksd.core.domain.model.PhotoAttachment
import com.bksd.core.domain.model.VideoAttachment
import com.bksd.insights.domain.model.InsightsMetrics
import com.bksd.insights.domain.model.InsightsPlaceKind
import com.bksd.insights.domain.model.InsightsRange
import com.bksd.insights.domain.model.MediaMix
import com.bksd.insights.domain.model.PlaceCount
import com.bksd.insights.domain.model.StreakRun
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock

class InsightsCalculator(
    private val clock: Clock,
    private val timeZone: TimeZone,
) {

    fun compute(moments: List<Moment>, range: InsightsRange): InsightsMetrics {
        if (moments.isEmpty()) return EMPTY

        val today = clock.todayIn(timeZone)
        val dates = moments.map { it.localDate() }.distinct().sorted()
        val years = dates.map { it.year }.distinct().sortedDescending()

        val dailyRuns = dailyRuns(dates)
        val weeklyRuns = weeklyRuns(dates)

        val recentWeekly = weeklyRuns.maxByOrNull { it.end }
        val currentWeekly = recentWeekly?.takeIf {
            val endMonday = mondayOf(it.end)
            val thisMonday = mondayOf(today)
            endMonday == thisMonday || endMonday == thisMonday.minus(7, DateTimeUnit.DAY)
        }
        val recentDaily = dailyRuns.maxByOrNull { it.end }
        val currentDaily = recentDaily?.takeIf {
            it.end == today || it.end == today.minus(1, DateTimeUnit.DAY)
        }

        val effectiveRange = when {
            years.size == 1 -> InsightsRange.Year(years.first())
            range is InsightsRange.Year && range.year in years -> range
            else -> InsightsRange.AllTime
        }
        val filtered = when (effectiveRange) {
            InsightsRange.AllTime -> moments
            is InsightsRange.Year -> moments.filter { it.localDate().year == effectiveRange.year }
        }
        val barData = barData(filtered, effectiveRange)

        return InsightsMetrics(
            hasActiveStreak = currentWeekly != null,
            currentWeekly = currentWeekly,
            currentDaily = currentDaily,
            longestDaily = dailyRuns.maxWithOrNull(compareBy({ it.length }, { it.end })),
            longestWeekly = weeklyRuns.maxWithOrNull(compareBy({ it.length }, { it.end })),
            recentDaily = recentDaily,
            recentWeekly = recentWeekly,
            entriesTotal = filtered.size,
            bars = barData.counts,
            barAxisLabels = barData.labels,
            mediaMix = mediaMix(filtered),
            writtenWords = filtered.sumOf { it.wordCount() },
            journaledDays = filtered.map { it.localDate() }.distinct().size,
            journaledThisMonth = moments.map { it.localDate() }
                .filter { it.year == today.year && it.month == today.month }
                .distinct().size,
            journaledThisYear = moments.map { it.localDate() }
                .filter { it.year == today.year }
                .distinct().size,
            places = places(filtered),
            years = years,
        )
    }

    private fun Moment.localDate(): LocalDate = createdAt.toLocalDateTime(timeZone).date

    private fun Moment.wordCount(): Int =
        body?.trim()?.takeIf { it.isNotEmpty() }?.split(WHITESPACE)?.size ?: 0

    private fun dailyRuns(dates: List<LocalDate>): List<StreakRun> {
        if (dates.isEmpty()) return emptyList()
        val runs = mutableListOf<StreakRun>()
        var start = dates.first()
        var prev = dates.first()
        var length = 1
        for (i in 1 until dates.size) {
            val cur = dates[i]
            if (prev.plus(1, DateTimeUnit.DAY) == cur) {
                length++
            } else {
                runs.add(StreakRun(length, start, prev))
                start = cur
                length = 1
            }
            prev = cur
        }
        runs.add(StreakRun(length, start, prev))
        return runs
    }

    private fun weeklyRuns(dates: List<LocalDate>): List<StreakRun> {
        if (dates.isEmpty()) return emptyList()
        val firstEntry = mutableMapOf<LocalDate, LocalDate>()
        val lastEntry = mutableMapOf<LocalDate, LocalDate>()
        for (d in dates) {
            val monday = mondayOf(d)
            firstEntry[monday] = firstEntry[monday]?.let { minOf(it, d) } ?: d
            lastEntry[monday] = lastEntry[monday]?.let { maxOf(it, d) } ?: d
        }
        val mondays = firstEntry.keys.sorted()
        val runs = mutableListOf<StreakRun>()
        var startMonday = mondays.first()
        var prevMonday = mondays.first()
        var length = 1
        for (i in 1 until mondays.size) {
            val cur = mondays[i]
            if (prevMonday.plus(7, DateTimeUnit.DAY) == cur) {
                length++
            } else {
                runs.add(StreakRun(length, firstEntry.getValue(startMonday), lastEntry.getValue(prevMonday)))
                startMonday = cur
                length = 1
            }
            prevMonday = cur
        }
        runs.add(StreakRun(length, firstEntry.getValue(startMonday), lastEntry.getValue(prevMonday)))
        return runs
    }

    private fun mondayOf(date: LocalDate): LocalDate =
        date.minus(date.dayOfWeek.ordinal, DateTimeUnit.DAY)

    private fun barData(filtered: List<Moment>, range: InsightsRange): BarData {
        if (filtered.isEmpty()) return BarData(emptyList(), emptyList())
        val monthIndices = filtered.map { val d = it.localDate(); d.year * 12 + d.month.ordinal }
        val startIndex: Int
        val endIndex: Int
        when (range) {
            is InsightsRange.Year -> {
                startIndex = range.year * 12
                endIndex = range.year * 12 + 11
            }

            InsightsRange.AllTime -> {
                endIndex = monthIndices.max()
                startIndex = maxOf(monthIndices.min(), endIndex - (MAX_BARS - 1))
            }
        }
        val counts = IntArray(endIndex - startIndex + 1)
        for (index in monthIndices) {
            if (index in startIndex..endIndex) counts[index - startIndex]++
        }
        val labels = when (range) {
            is InsightsRange.Year -> MONTH_INITIALS
            InsightsRange.AllTime -> ((startIndex / 12)..(endIndex / 12)).map { it.toString() }
        }
        return BarData(counts.toList(), labels)
    }

    private data class BarData(val counts: List<Int>, val labels: List<String>)

    private fun mediaMix(filtered: List<Moment>): MediaMix = MediaMix(
        photos = filtered.sumOf { m -> m.attachments.count { it is PhotoAttachment } },
        videos = filtered.sumOf { m -> m.attachments.count { it is VideoAttachment } },
        voiceNotes = filtered.sumOf { m -> m.attachments.count { it is AudioAttachment } },
        places = filtered.count { it.location != null },
    )

    private fun places(filtered: List<Moment>): List<PlaceCount> =
        filtered
            .mapNotNull { it.location?.displayName?.let(::stateOf) }
            .groupBy { it.lowercase() }
            .map { (_, names) -> PlaceCount(names.first(), names.size, kindOf(names.first())) }
            .sortedByDescending { it.count }
            .take(4)

    private fun stateOf(displayName: String): String? {
        val trimmed = displayName.trim()
        if (trimmed.isBlank()) return null
        return trimmed.substringAfterLast(",").trim().takeIf { it.isNotBlank() }
    }

    private fun kindOf(name: String): InsightsPlaceKind {
        val words = name.lowercase().split(NON_LETTER).toSet()
        return when {
            words.any { it in BEACH_WORDS } -> InsightsPlaceKind.BEACH
            words.any { it in PARK_WORDS } -> InsightsPlaceKind.PARK
            words.any { it in RESTAURANT_WORDS } -> InsightsPlaceKind.RESTAURANT
            words.any { it in LANDMARK_WORDS } -> InsightsPlaceKind.LANDMARK
            else -> InsightsPlaceKind.GENERIC
        }
    }

    private companion object {
        private const val MAX_BARS = 16
        private val MONTH_INITIALS = listOf("J", "F", "M", "A", "M", "J", "J", "A", "S", "O", "N", "D")
        private val WHITESPACE = Regex("\\s+")
        private val NON_LETTER = Regex("[^a-z]+")
        private val BEACH_WORDS = setOf("beach", "shore", "coast", "bay")
        private val PARK_WORDS = setOf("park", "garden", "trail", "forest", "lake", "mountain")
        private val RESTAURANT_WORDS = setOf("restaurant", "cafe", "coffee", "bar", "diner", "bistro", "pub")
        private val LANDMARK_WORDS = setOf("museum", "monument", "landmark", "tower", "castle", "cathedral", "temple", "palace")
        private val EMPTY = InsightsMetrics(
            hasActiveStreak = false,
            currentWeekly = null,
            currentDaily = null,
            longestDaily = null,
            longestWeekly = null,
            recentDaily = null,
            recentWeekly = null,
            entriesTotal = 0,
            bars = emptyList(),
            barAxisLabels = emptyList(),
            mediaMix = MediaMix(0, 0, 0, 0),
            writtenWords = 0,
            journaledDays = 0,
            journaledThisMonth = 0,
            journaledThisYear = 0,
            places = emptyList(),
            years = emptyList(),
        )
    }
}
