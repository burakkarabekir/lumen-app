package com.bksd.reflection.domain.analysis

import com.bksd.reflection.domain.model.EntryAnalysis

data class ReflectionResponse(
    val analysis: EntryAnalysis,
    val feedback: String?,
    val question: String?,
    val coverImageUrl: String?,
)
