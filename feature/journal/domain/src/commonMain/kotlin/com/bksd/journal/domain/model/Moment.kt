package com.bksd.journal.domain.model

import com.bksd.core.domain.location.LocationData
import com.bksd.core.domain.model.Attachment
import kotlin.time.Instant

data class Moment(
    val id: String,
    val title: String,
    val body: String?,
    val createdAt: Instant,
    val moods: List<Mood> = emptyList(),
    val tags: List<String> = emptyList(),
    val attachments: List<Attachment> = emptyList(),
    val location: LocationData? = null,
    val pendingSync: Boolean = false
)

enum class Mood(val label: String, val emoji: String) {
    HAPPY("Happy", "😊"),
    CALM("Calm", "🧘"),
    INSPIRED("Inspired", "✨"),
    ENERGETIC("Energetic", "⚡"),
    REFLECTIVE("Reflective", "🤔"),
    GRATEFUL("Grateful", "🙏"),
    CREATIVE("Creative", "🎨"),
    FOCUSED("Focused", "🎯"),
    TIRED("Tired", "😫"),
    ANXIOUS("Anxious", "😰"),
    ROMANTIC("Romantic", "💕"),
    MELANCHOLIC("Melancholic", "🌧️"),
    PROUD("Proud", "🏆"),
    HOPEFUL("Hopeful", "🌱"),
    NOSTALGIC("Nostalgic", "📷")
}
