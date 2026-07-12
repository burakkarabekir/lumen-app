package com.bksd.insights.presentation

import kotlinx.datetime.Month
import org.jetbrains.compose.resources.StringResource

internal fun monthAbbrRes(month: Month): StringResource = when (month) {
    Month.JANUARY -> Res.string.month_abbr_jan
    Month.FEBRUARY -> Res.string.month_abbr_feb
    Month.MARCH -> Res.string.month_abbr_mar
    Month.APRIL -> Res.string.month_abbr_apr
    Month.MAY -> Res.string.month_abbr_may
    Month.JUNE -> Res.string.month_abbr_jun
    Month.JULY -> Res.string.month_abbr_jul
    Month.AUGUST -> Res.string.month_abbr_aug
    Month.SEPTEMBER -> Res.string.month_abbr_sep
    Month.OCTOBER -> Res.string.month_abbr_oct
    Month.NOVEMBER -> Res.string.month_abbr_nov
    Month.DECEMBER -> Res.string.month_abbr_dec
}
