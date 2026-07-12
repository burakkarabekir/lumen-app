package com.bksd.core.presentation

import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource

@Composable
fun monthDayLabel(date: LocalDate): String =
    stringResource(Res.string.fmt_month_day, stringResource(monthFullRes(date.month)), date.day)

@Composable
fun shortDateLabel(date: LocalDate): String =
    stringResource(Res.string.fmt_short_date, stringResource(monthShortRes(date.month)), date.day, date.year)

@Composable
fun weekdayDateLabel(date: LocalDate): String =
    stringResource(
        Res.string.fmt_weekday_date,
        stringResource(weekdayFullRes(date.dayOfWeek)),
        stringResource(monthFullRes(date.month)),
        date.day,
    )
