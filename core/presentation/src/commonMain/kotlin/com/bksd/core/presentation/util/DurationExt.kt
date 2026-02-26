package com.bksd.core.presentation.util

import kotlin.time.Duration

private const val SECONDS_PER_MINUTE = 60

/**
 * Formats a [Duration] as MM:SS string.
 * Negative durations are treated as 00:00.
 * Durations over 59:59 will show total minutes (e.g., 65:30).
 *
 * @return Formatted string in MM:SS format (e.g., "05:30")
 */
fun Duration.formatMMSS(): String {
    val totalSeconds = inWholeSeconds.coerceAtLeast(0L)
    val minutes = totalSeconds / SECONDS_PER_MINUTE
    val seconds = totalSeconds % SECONDS_PER_MINUTE

    return "${minutes.padZero()}:${seconds.padZero()}"
}

/**
 * Pads a number with leading zero if less than 10.
 */
private fun Long.padZero(): String = if (this < 10) "0$this" else toString()
