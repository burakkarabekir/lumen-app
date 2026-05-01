package com.bksd.journal.domain.model

import com.bksd.core.domain.location.LocationData
import com.bksd.core.domain.model.Attachment
import kotlin.time.Instant

data class Moment(
    val id: String,
    val body: String?,
    val createdAt: Instant,
    val mood: Mood,
    val tags: List<String> = emptyList(),
    val attachments: List<Attachment> = emptyList(),
    val location: LocationData? = null,
    val pendingSync: Boolean = false
)

enum class Mood(val label: String, val emoji: String) {
    INSPIRED("Inspired", "✨"),
    CALM("Calm", "😊"),
    REFLECTIVE("Reflective", "🤔"),
    ENERGETIC("Energetic", "⚡"),
    TIRED("Tired", "😫")
}
