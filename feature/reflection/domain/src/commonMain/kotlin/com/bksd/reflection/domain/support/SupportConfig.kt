package com.bksd.reflection.domain.support

import kotlinx.serialization.Serializable

@Serializable
data class SupportResource(
    val label: String,
    val phoneNumber: String
)

object SupportConfig {
    const val ELEVATED_MESSAGE: String =
        "It sounds like things feel heavy right now, and that's hard. You don't have to carry it alone — " +
            "reaching out to someone you trust, or a mental-health professional, can make a difference. " +
            "Be gentle with yourself today."

    const val CRISIS_MESSAGE: String =
        "If you're thinking about harming yourself or feel unsafe, please reach out for help right now. " +
            "You deserve support, and people are available to talk with you."

    // 112 is Turkey's general emergency number (verified).
    val emergency: SupportResource = SupportResource(label = "Emergency", phoneNumber = "112")

    // TODO(you): Add VERIFIED, current Turkish suicide-prevention / crisis lines before shipping.
    //  Do NOT add unverified numbers — kept empty intentionally until you confirm them.
    val crisisLines: List<SupportResource> = emptyList()

    // TODO(you): Add VERIFIED Turkish mental-health support lines before shipping.
    val mentalHealthLines: List<SupportResource> = emptyList()
}
