package com.bksd.reflection.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ReflectionTheme(
    val label: String,
    val colorHex: String,
    val count: Int = 0
)

@Serializable
data class WeeklyReflection(
    val narrative: String,
    val summary: String = "",
    val themes: List<ReflectionTheme>,
    val questions: List<String> = emptyList(),
    val entryCount: Int,
    val rangeLabel: String,
    val generatedAtMs: Long
)
