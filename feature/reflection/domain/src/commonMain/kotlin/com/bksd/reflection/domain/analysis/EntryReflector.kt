package com.bksd.reflection.domain.analysis

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result

interface EntryReflector {
    suspend fun reflect(
        momentId: String,
        entryText: String,
        moods: List<String>,
        trend: String?
    ): Result<ReflectionResponse, AppError>
}
