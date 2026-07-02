package com.bksd.reflection.domain.usecase

import com.bksd.reflection.domain.model.EntryAnalysis

class TrendSummaryBuilder {

    fun build(recent: List<EntryAnalysis>): String? {
        if (recent.size < MIN_FOR_TREND) return null

        val count = recent.size
        val dominantValence = recent
            .groupingBy { it.moodValence }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key

        val topThemes = recent
            .flatMap { it.themes }
            .groupingBy { it }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .take(MAX_THEMES)
            .map { it.key }

        return buildString {
            append("Last ").append(count).append(" entries")
            if (dominantValence != null) {
                append(": mood mostly ").append(dominantValence.name.lowercase().replace('_', ' '))
            }
            if (topThemes.isNotEmpty()) {
                append("; recurring themes: ").append(topThemes.joinToString(", "))
            }
            append('.')
        }
    }

    companion object {
        private const val MIN_FOR_TREND = 3
        private const val MAX_THEMES = 3
    }
}
