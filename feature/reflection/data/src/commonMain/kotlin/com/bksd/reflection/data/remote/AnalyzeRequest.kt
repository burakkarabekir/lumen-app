package com.bksd.reflection.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class AnalyzeRequest(
    val text: String,
    val mood: String? = null,
    val trend: String? = null,
    val language: String? = null
)
