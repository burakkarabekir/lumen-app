package com.bksd.reflection.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class AnalyzeRequest(
    val momentId: String,
    val text: String,
    val moods: List<String>? = null,
    val mood: String? = null,
    val trend: String? = null,
    val language: String? = null
)
