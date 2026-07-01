package com.bksd.reflection.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class WeeklyThemeDto(
    val label: String,
    val count: Int = 1
)

@Serializable
data class WeeklyReflectionDto(
    val narrative: String,
    val summary: String = "",
    val themes: List<WeeklyThemeDto> = emptyList(),
    val questions: List<String> = emptyList()
)
