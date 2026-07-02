package com.bksd.journal.presentation.util

import androidx.compose.runtime.Stable
import com.bksd.core.presentation.util.durationToText
import com.bksd.core.presentation.util.toFormattedTime
import kotlinx.datetime.TimeZone
import kotlin.time.Instant

@Stable
interface MomentFormatter {
    fun formatTime(instant: Instant): String
    fun formatDuration(ms: Long): String
}

class DefaultMomentFormatter(
    private val timeZone: TimeZone
) : MomentFormatter {
    override fun formatTime(instant: Instant): String {
        return instant.toFormattedTime(timeZone)
    }

    override fun formatDuration(ms: Long): String {
        return durationToText(ms)
    }
}
