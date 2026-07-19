package com.bksd.reflection.domain.analysis

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.reflection.domain.model.MomentReflection

data class StoredReflection(
    val momentId: String,
    val reflection: MomentReflection,
)

interface EntryReflectionSource {
    suspend fun fetchAll(): Result<List<StoredReflection>, AppError>
}
