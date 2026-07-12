package com.bksd.insights.presentation

import kotlinx.datetime.DayOfWeek
import org.jetbrains.compose.resources.StringResource

internal fun DayOfWeek.initialRes(): StringResource = when (this) {
    DayOfWeek.MONDAY -> Res.string.day_initial_monday
    DayOfWeek.TUESDAY -> Res.string.day_initial_tuesday
    DayOfWeek.WEDNESDAY -> Res.string.day_initial_wednesday
    DayOfWeek.THURSDAY -> Res.string.day_initial_thursday
    DayOfWeek.FRIDAY -> Res.string.day_initial_friday
    DayOfWeek.SATURDAY -> Res.string.day_initial_saturday
    DayOfWeek.SUNDAY -> Res.string.day_initial_sunday
}

internal fun DayOfWeek.shortNameRes(): StringResource = when (this) {
    DayOfWeek.MONDAY -> Res.string.day_short_monday
    DayOfWeek.TUESDAY -> Res.string.day_short_tuesday
    DayOfWeek.WEDNESDAY -> Res.string.day_short_wednesday
    DayOfWeek.THURSDAY -> Res.string.day_short_thursday
    DayOfWeek.FRIDAY -> Res.string.day_short_friday
    DayOfWeek.SATURDAY -> Res.string.day_short_saturday
    DayOfWeek.SUNDAY -> Res.string.day_short_sunday
}
