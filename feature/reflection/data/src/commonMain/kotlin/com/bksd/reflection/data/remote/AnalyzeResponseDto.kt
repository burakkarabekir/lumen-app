package com.bksd.reflection.data.remote

import com.bksd.reflection.domain.model.EntryAnalysis
import kotlinx.serialization.Serializable

@Serializable
data class AnalyzeResponseDto(
    val analysis: EntryAnalysis,
    val feedback: String? = null,
    val question: String? = null,
    val coverImageUrl: String? = null,
)
