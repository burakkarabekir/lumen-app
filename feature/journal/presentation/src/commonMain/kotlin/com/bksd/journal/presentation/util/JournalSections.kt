package com.bksd.journal.presentation.util

import com.bksd.core.domain.model.Moment
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.journal.JournalSection
import com.bksd.journal.presentation.model.toMomentUi
import com.bksd.journal.presentation.month_april
import com.bksd.journal.presentation.month_august
import com.bksd.journal.presentation.month_december
import com.bksd.journal.presentation.month_february
import com.bksd.journal.presentation.month_january
import com.bksd.journal.presentation.month_july
import com.bksd.journal.presentation.month_june
import com.bksd.journal.presentation.month_march
import com.bksd.journal.presentation.month_may
import com.bksd.journal.presentation.month_november
import com.bksd.journal.presentation.month_october
import com.bksd.journal.presentation.month_september
import com.bksd.journal.presentation.section_today
import com.bksd.journal.presentation.section_yesterday
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.getString

suspend fun groupMomentsByDate(
    moments: List<Moment>,
    today: LocalDate,
    timeZone: TimeZone,
): List<JournalSection> {
    if (moments.isEmpty()) return emptyList()
    val yesterday = today.minus(1, DateTimeUnit.DAY)
    return moments
        .groupBy { headerFor(it.createdAt.toLocalDateTime(timeZone).date, today, yesterday) }
        .map { (header, items) -> JournalSection(header, items.map { it.toMomentUi() }.toImmutableList()) }
}

private suspend fun headerFor(date: LocalDate, today: LocalDate, yesterday: LocalDate): String = when {
    date == today -> getString(Res.string.section_today)
    date == yesterday -> getString(Res.string.section_yesterday)
    date.year == today.year -> monthName(date.month)
    else -> "${monthName(date.month)} ${date.year}"
}

private suspend fun monthName(month: Month): String = getString(
    when (month) {
        Month.JANUARY -> Res.string.month_january
        Month.FEBRUARY -> Res.string.month_february
        Month.MARCH -> Res.string.month_march
        Month.APRIL -> Res.string.month_april
        Month.MAY -> Res.string.month_may
        Month.JUNE -> Res.string.month_june
        Month.JULY -> Res.string.month_july
        Month.AUGUST -> Res.string.month_august
        Month.SEPTEMBER -> Res.string.month_september
        Month.OCTOBER -> Res.string.month_october
        Month.NOVEMBER -> Res.string.month_november
        Month.DECEMBER -> Res.string.month_december
    }
)
