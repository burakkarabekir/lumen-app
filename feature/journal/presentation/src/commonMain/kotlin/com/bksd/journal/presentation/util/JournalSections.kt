package com.bksd.journal.presentation.util

import com.bksd.core.domain.model.Moment
import com.bksd.journal.presentation.journal.JournalSection
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

fun groupMomentsByDate(
    moments: List<Moment>,
    today: LocalDate,
    timeZone: TimeZone,
): List<JournalSection> {
    if (moments.isEmpty()) return emptyList()
    val yesterday = today.minus(1, DateTimeUnit.DAY)
    return moments
        .groupBy { headerFor(it.createdAt.toLocalDateTime(timeZone).date, today, yesterday) }
        .map { (header, items) -> JournalSection(header, items.toImmutableList()) }
}

private fun headerFor(date: LocalDate, today: LocalDate, yesterday: LocalDate): String = when {
    date == today -> "Today"
    date == yesterday -> "Yesterday"
    date.year == today.year -> monthName(date)
    else -> "${monthName(date)} ${date.year}"
}

private fun monthName(date: LocalDate): String =
    date.month.name.lowercase().replaceFirstChar { it.uppercase() }
