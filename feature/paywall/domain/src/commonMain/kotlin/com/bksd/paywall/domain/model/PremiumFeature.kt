package com.bksd.paywall.domain.model

data class PremiumFeature(
    val title: String,
    val description: String,
    val icon: FeatureIcon
)

enum class FeatureIcon {
    MULTIMEDIA,
    AI_REFLECTION,
    ANALYTICS
}
