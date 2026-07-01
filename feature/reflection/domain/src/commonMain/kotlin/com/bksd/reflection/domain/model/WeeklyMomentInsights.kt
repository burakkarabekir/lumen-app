package com.bksd.reflection.domain.model

data class ArcPoint(
    val dayLabel: String,
    val hasEntry: Boolean,
    val valence: Float,
    val colorHex: String?
)

data class StandoutEntry(
    val momentId: String,
    val title: String,
    val quote: String,
    val colorHex: String
)

data class WeeklyMomentInsights(
    val arc: List<ArcPoint>,
    val brightestDayLabel: String?,
    val standout: StandoutEntry?
)
