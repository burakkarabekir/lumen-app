package com.bksd.insights.domain.model

import kotlin.time.Instant

data class Place(
    val name: String,
    val count: Int,
    val lastEntry: Instant,
)
