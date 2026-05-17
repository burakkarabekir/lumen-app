package com.bksd.journal.presentation.journal.components

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal fun formatCardDate(instant: kotlin.time.Instant, timeZone: TimeZone): String {
    val ldt = instant.toLocalDateTime(timeZone)
    val dayOfWeek = ldt.dayOfWeek.name
        .lowercase()
        .replaceFirstChar { it.uppercase() }
    val month = ldt.month.name
        .lowercase()
        .replaceFirstChar { it.uppercase() }
    return "$dayOfWeek, $month ${ldt.date.day}"
}
