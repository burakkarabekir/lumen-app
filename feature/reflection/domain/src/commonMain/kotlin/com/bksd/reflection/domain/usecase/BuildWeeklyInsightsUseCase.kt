package com.bksd.reflection.domain.usecase

import com.bksd.core.domain.model.Moment
import com.bksd.reflection.domain.analysis.moodColorHex
import com.bksd.reflection.domain.analysis.moodValence
import com.bksd.reflection.domain.model.ArcPoint
import com.bksd.reflection.domain.model.StandoutEntry
import com.bksd.reflection.domain.model.WeeklyMomentInsights
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class BuildWeeklyInsightsUseCase(
    private val clock: Clock,
    private val timeZone: TimeZone,
) {
    operator fun invoke(moments: List<Moment>): WeeklyMomentInsights {
        val today = clock.now().toLocalDateTime(timeZone).date
        val monday = today.minus(today.dayOfWeek.isoDayNumber - 1, DateTimeUnit.DAY)
        val weekDates = (0..6).map { monday.plus(it, DateTimeUnit.DAY) }
        val byDate = moments.groupBy { it.createdAt.toLocalDateTime(timeZone).date }

        val points = weekDates.map { date ->
            val moods = byDate[date].orEmpty().flatMap { it.moods }
            val point = if (moods.isEmpty()) {
                ArcPoint(dayInitial(date.dayOfWeek), hasEntry = false, valence = 0f, colorHex = null)
            } else {
                val valence = moods.map(::moodValence).average().toFloat()
                val dominant = moods.groupingBy { it }.eachCount().maxByOrNull { it.value }!!.key
                ArcPoint(dayInitial(date.dayOfWeek), hasEntry = true, valence, moodColorHex(dominant))
            }
            date to point
        }

        val brightest = points.filter { it.second.hasEntry }.maxByOrNull { it.second.valence }

        val standout = moments
            .filter { it.createdAt.toLocalDateTime(timeZone).date in weekDates && hasText(it) }
            .maxWithOrNull(compareBy({ entryValence(it) }, { wordCount(it) }))
            ?.let { moment ->
                StandoutEntry(
                    momentId = moment.id,
                    title = moment.title.ifBlank { "Untitled" },
                    quote = quoteFrom(moment),
                    colorHex = moment.moods.firstOrNull()?.let(::moodColorHex) ?: DEFAULT_COLOR
                )
            }

        return WeeklyMomentInsights(
            arc = points.map { it.second },
            brightestDayLabel = brightest?.let { shortDayName(it.first.dayOfWeek) },
            standout = standout
        )
    }

    private fun entryValence(moment: Moment): Float =
        moment.moods.takeIf { it.isNotEmpty() }?.map(::moodValence)?.average()?.toFloat() ?: 0.5f

    private fun hasText(moment: Moment): Boolean =
        moment.title.isNotBlank() || !moment.body.isNullOrBlank()

    private fun wordCount(moment: Moment): Int =
        source(moment).split(Regex("\\s+")).count { it.isNotBlank() }

    private fun source(moment: Moment): String =
        moment.body?.takeIf { it.isNotBlank() } ?: moment.title

    private fun quoteFrom(moment: Moment): String {
        val text = source(moment).trim()
        val first = text.split(Regex("(?<=[.!?])\\s+")).firstOrNull()?.trim().orEmpty()
        val quote = first.ifBlank { text }
        return if (quote.length > 140) quote.take(137).trimEnd() + "…" else quote
    }

    private fun dayInitial(day: DayOfWeek): String = when (day) {
        DayOfWeek.MONDAY -> "M"
        DayOfWeek.TUESDAY -> "T"
        DayOfWeek.WEDNESDAY -> "W"
        DayOfWeek.THURSDAY -> "T"
        DayOfWeek.FRIDAY -> "F"
        DayOfWeek.SATURDAY -> "S"
        DayOfWeek.SUNDAY -> "S"
    }

    private fun shortDayName(day: DayOfWeek): String = when (day) {
        DayOfWeek.MONDAY -> "Mon"
        DayOfWeek.TUESDAY -> "Tue"
        DayOfWeek.WEDNESDAY -> "Wed"
        DayOfWeek.THURSDAY -> "Thu"
        DayOfWeek.FRIDAY -> "Fri"
        DayOfWeek.SATURDAY -> "Sat"
        DayOfWeek.SUNDAY -> "Sun"
    }

    private companion object {
        const val DEFAULT_COLOR = "#7682D6"
    }
}
