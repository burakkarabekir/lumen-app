package com.bksd.journal.domain.model

import com.bksd.core.domain.location.LocationData
import com.bksd.core.domain.model.Attachment
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Instant

data class Moment(
    val id: String,
    val body: String?, // Title removed per Stitch design
    val createdAt: Instant,
    val mood: Mood,
    val tags: PersistentList<String> = persistentListOf(),
    val attachments: PersistentList<Attachment> = persistentListOf(),
    val location: LocationData? = null
)

enum class Mood(val label: String, val emoji: String) {
    INSPIRED("Inspired", "✨"),
    CALM("Calm", "😊"),
    REFLECTIVE("Reflective", "🤔"),
    ENERGETIC("Energetic", "⚡"),
    TIRED("Tired", "😫")
}
