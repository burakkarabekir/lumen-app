package com.bksd.reflection.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class DistressLevel {
    NONE,
    MILD,
    ELEVATED,
    CRISIS
}
