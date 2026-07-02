package com.bksd.reflection.domain

import com.bksd.reflection.domain.model.DistressLevel
import com.bksd.reflection.domain.model.EntryAnalysis
import com.bksd.reflection.domain.model.MoodValence
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class EntryAnalysisParsingTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun parsesWellFormedAnalysisJson() {
        val raw = """
            {
              "summary": "Described a busy workday and felt tired by evening.",
              "moodValence": "LOW",
              "moodConfidence": 0.6,
              "dominantEmotions": ["tired", "stressed"],
              "themes": ["work", "fatigue"],
              "distress": "MILD",
              "distressRationale": "Ordinary end-of-day tiredness, no risk signals."
            }
        """.trimIndent()

        val analysis = json.decodeFromString<EntryAnalysis>(raw)

        assertEquals(MoodValence.LOW, analysis.moodValence)
        assertEquals(0.6, analysis.moodConfidence)
        assertEquals(listOf("tired", "stressed"), analysis.dominantEmotions)
        assertEquals(listOf("work", "fatigue"), analysis.themes)
        assertEquals(DistressLevel.MILD, analysis.distress)
    }

    @Test
    fun roundTripsThroughSerialization() {
        val original = EntryAnalysis(
            summary = "Neutral day.",
            moodValence = MoodValence.NEUTRAL,
            moodConfidence = 0.2,
            dominantEmotions = emptyList(),
            themes = listOf("routine"),
            distress = DistressLevel.NONE,
            distressRationale = "No distress indicators."
        )
        assertEquals(original, json.decodeFromString<EntryAnalysis>(json.encodeToString(original)))
    }

    @Test
    fun parsesEveryDistressLevel() {
        DistressLevel.entries.forEach { level ->
            val raw = """{"summary":"s","moodValence":"NEUTRAL","moodConfidence":0.1,""" +
                """"dominantEmotions":[],"themes":[],"distress":"${level.name}","distressRationale":"r"}"""
            assertEquals(level, json.decodeFromString<EntryAnalysis>(raw).distress)
        }
    }
}
