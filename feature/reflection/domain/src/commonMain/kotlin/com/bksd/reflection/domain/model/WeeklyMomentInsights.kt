package com.bksd.reflection.domain.model

import kotlinx.datetime.DayOfWeek

data class ArcPoint(
    val day: DayOfWeek,
    val hasEntry: Boolean,
    val valence: Float,
    val colorHex: String?
)

data class StandoutEntry(
    val momentId: String,
    val title: String,
    val quote: String,
    val colorHex: String?
)

data class WeeklyMomentInsights(
    val arc: List<ArcPoint>,
    val brightestDay: DayOfWeek?,
    val standout: StandoutEntry?
)
