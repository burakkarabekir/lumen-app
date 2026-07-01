package com.bksd.reflection.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class MoodValence {
    VERY_LOW,
    LOW,
    NEUTRAL,
    POSITIVE,
    VERY_POSITIVE
}
