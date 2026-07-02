package com.bksd.core.domain.model

import com.bksd.core.domain.location.LocationData
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
    val isFavorite: Boolean = false,
    val pendingSync: Boolean = false
)

enum class Mood(val label: String, val emoji: String) {
    HAPPY("Happy", "😊"),
    GRATEFUL("Grateful", "🙏"),
    CALM("Calm", "🧘"),
    LOVED("Loved", "🥰"),
    HOPEFUL("Hopeful", "🌱"),
    PROUD("Proud", "🏆"),
    INSPIRED("Inspired", "✨"),
    REFLECTIVE("Reflective", "🤔"),
    FOCUSED("Focused", "🎯"),
    NOSTALGIC("Nostalgic", "📷"),
    TIRED("Tired", "😫"),
    ANXIOUS("Anxious", "😰"),
    STRESSED("Stressed", "😖"),
    FRUSTRATED("Frustrated", "😤"),
    SAD("Sad", "😢"),
    MELANCHOLIC("Melancholic", "🌧️"),
    BORED("Bored", "🥱")
}
