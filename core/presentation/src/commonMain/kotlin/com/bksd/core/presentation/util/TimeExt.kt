package com.bksd.core.presentation.util

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

/**
 * Formats an [Instant] to a localized "HH:MM AM/PM" string.
 * Example output: "4:30 PM"
 */
fun Instant.toFormattedTime(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    val localTime = this.toLocalDateTime(timeZone)
    
    // Convert 24-hour hour to 12-hour format
    val hour = when {
        localTime.hour == 0 -> 12
        localTime.hour > 12 -> localTime.hour - 12
        else -> localTime.hour
    }
    
    val amPm = if (localTime.hour >= 12) "PM" else "AM"
    val minute = localTime.minute.toString().padStart(2, '0')
    
    return "$hour:$minute $amPm"
}

/**
 * Formats a duration in milliseconds to "M:SS" (e.g., "1:23").
 */
fun formatDuration(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "$minutes:${seconds.toString().padStart(2, '0')}"
}
