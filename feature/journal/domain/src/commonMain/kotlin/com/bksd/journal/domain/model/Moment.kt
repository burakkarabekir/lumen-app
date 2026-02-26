package com.bksd.journal.domain.model

import com.bksd.core.domain.location.LocationData
import com.bksd.core.domain.model.MediaAttachment

data class Moment(
    val id: String,
    val body: String?, // Title removed per Stitch design
    val timestamp: Long,
    val mood: Mood,
    val tags: List<String> = emptyList(),
    val attachments: List<MediaAttachment> = emptyList(),
    val links: List<String> = emptyList(),
    val location: LocationData? = null
)

enum class Mood(val label: String, val emoji: String) {
    INSPIRED("Inspired", "✨"),
    CALM("Calm", "😊"),
    REFLECTIVE("Reflective", "🤔"),
    ENERGETIC("Energetic", "⚡"),
    TIRED("Tired", "😫")
}
