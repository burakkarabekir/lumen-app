package com.bksd.reflection.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EntryReflectionDto(
    @SerialName("moment_id") val momentId: String,
    @SerialName("summary") val summary: String,
    @SerialName("mood_valence") val moodValence: String,
    @SerialName("mood_confidence") val moodConfidence: Double,
    @SerialName("dominant_emotions") val dominantEmotions: List<String> = emptyList(),
    @SerialName("themes") val themes: List<String> = emptyList(),
    @SerialName("distress") val distress: String,
    @SerialName("feedback") val feedback: String? = null,
    @SerialName("question") val question: String? = null,
    @SerialName("cover_image_url") val coverImageUrl: String? = null,
)
