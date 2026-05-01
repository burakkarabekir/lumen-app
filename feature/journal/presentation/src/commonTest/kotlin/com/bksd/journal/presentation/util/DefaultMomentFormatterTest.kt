package com.bksd.journal.presentation.util

import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class DefaultMomentFormatterTest {

    private val timeZone = TimeZone.of("UTC")
    private val formatter = DefaultMomentFormatter(timeZone)

    @Test
    fun testFormatTime_Morning() {
        val instant = Instant.parse("2024-05-01T08:30:00Z")
        assertEquals("8:30 AM", formatter.formatTime(instant))
    }

    @Test
    fun testFormatTime_Afternoon() {
        val instant = Instant.parse("2024-05-01T14:45:00Z")
        assertEquals("2:45 PM", formatter.formatTime(instant))
    }

    @Test
    fun testFormatTime_Midnight() {
        val instant = Instant.parse("2024-05-01T00:05:00Z")
        assertEquals("12:05 AM", formatter.formatTime(instant))
    }

    @Test
    fun testFormatTime_Noon() {
        val instant = Instant.parse("2024-05-01T12:00:00Z")
        assertEquals("12:00 PM", formatter.formatTime(instant))
    }
}
