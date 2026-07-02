package com.bksd.journal.presentation.detail.share

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

fun shareCardDateLabel(instant: Instant): String {
    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val dayOfWeek = dateTime.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
    val month = dateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }
    return "$dayOfWeek, $month ${dateTime.day}"
}
