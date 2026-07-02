package com.bksd.reflection.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class EntryAnalysis(
    val summary: String,
    val moodValence: MoodValence,
    val moodConfidence: Double,
    val dominantEmotions: List<String>,
    val themes: List<String>,
    val distress: DistressLevel,
    val distressRationale: String
)
