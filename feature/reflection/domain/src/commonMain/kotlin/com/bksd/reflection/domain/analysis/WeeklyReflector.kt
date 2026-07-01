package com.bksd.reflection.domain.analysis

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result

data class WeeklyThemeContent(
    val label: String,
    val count: Int
)

data class WeeklyReflectionContent(
    val narrative: String,
    val summary: String,
    val themes: List<WeeklyThemeContent>,
    val questions: List<String>
)

interface WeeklyReflector {
    suspend fun reflectWeek(entries: List<String>): Result<WeeklyReflectionContent, AppError>
}
