package com.bksd.core.presentation

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

internal fun monthFullRes(month: Month): StringResource = when (month) {
    Month.JANUARY -> Res.string.month_full_jan
    Month.FEBRUARY -> Res.string.month_full_feb
    Month.MARCH -> Res.string.month_full_mar
    Month.APRIL -> Res.string.month_full_apr
    Month.MAY -> Res.string.month_full_may
    Month.JUNE -> Res.string.month_full_jun
    Month.JULY -> Res.string.month_full_jul
    Month.AUGUST -> Res.string.month_full_aug
    Month.SEPTEMBER -> Res.string.month_full_sep
    Month.OCTOBER -> Res.string.month_full_oct
    Month.NOVEMBER -> Res.string.month_full_nov
    Month.DECEMBER -> Res.string.month_full_dec
}

internal fun monthShortRes(month: Month): StringResource = when (month) {
    Month.JANUARY -> Res.string.month_short_jan
    Month.FEBRUARY -> Res.string.month_short_feb
    Month.MARCH -> Res.string.month_short_mar
    Month.APRIL -> Res.string.month_short_apr
    Month.MAY -> Res.string.month_short_may
    Month.JUNE -> Res.string.month_short_jun
    Month.JULY -> Res.string.month_short_jul
    Month.AUGUST -> Res.string.month_short_aug
    Month.SEPTEMBER -> Res.string.month_short_sep
    Month.OCTOBER -> Res.string.month_short_oct
    Month.NOVEMBER -> Res.string.month_short_nov
    Month.DECEMBER -> Res.string.month_short_dec
}

internal fun weekdayFullRes(day: DayOfWeek): StringResource = when (day) {
    DayOfWeek.MONDAY -> Res.string.weekday_full_mon
    DayOfWeek.TUESDAY -> Res.string.weekday_full_tue
    DayOfWeek.WEDNESDAY -> Res.string.weekday_full_wed
    DayOfWeek.THURSDAY -> Res.string.weekday_full_thu
    DayOfWeek.FRIDAY -> Res.string.weekday_full_fri
    DayOfWeek.SATURDAY -> Res.string.weekday_full_sat
    DayOfWeek.SUNDAY -> Res.string.weekday_full_sun
}

suspend fun formatMonthDay(date: LocalDate): String =
    getString(Res.string.fmt_month_day, getString(monthFullRes(date.month)), date.day)

suspend fun formatShortDate(date: LocalDate): String =
    getString(Res.string.fmt_short_date, getString(monthShortRes(date.month)), date.day, date.year)

suspend fun formatWeekdayDate(date: LocalDate): String =
    getString(
        Res.string.fmt_weekday_date,
        getString(weekdayFullRes(date.dayOfWeek)),
        getString(monthFullRes(date.month)),
        date.day,
    )
