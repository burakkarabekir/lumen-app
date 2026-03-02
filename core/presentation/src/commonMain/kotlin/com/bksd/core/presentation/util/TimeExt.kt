package com.bksd.core.presentation.util

import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Formats an [Instant] to a localized "HH:MM AM/PM" string.
 * Example output: "4:30 PM"
 */
fun Instant.toFormattedTime(): String {
    val localTime = this.toLocalDateTime(TimeZone.currentSystemDefault())
    
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
