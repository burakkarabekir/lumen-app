package com.bksd.reflection.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class WeeklyReflectionRequest(
    val entries: List<String>,
    val language: String? = null
)
