package com.bksd.reflection.domain.analysis

import com.bksd.core.domain.model.Mood

fun moodValence(mood: Mood): Float = when (mood) {
    Mood.LOVED -> 0.92f
    Mood.HAPPY -> 0.90f
    Mood.PROUD -> 0.86f
    Mood.GRATEFUL -> 0.85f
    Mood.INSPIRED -> 0.84f
    Mood.HOPEFUL -> 0.80f
    Mood.CALM -> 0.72f
    Mood.FOCUSED -> 0.66f
    Mood.REFLECTIVE -> 0.55f
    Mood.NOSTALGIC -> 0.52f
    Mood.BORED -> 0.40f
    Mood.TIRED -> 0.34f
    Mood.MELANCHOLIC -> 0.30f
    Mood.FRUSTRATED -> 0.28f
    Mood.ANXIOUS -> 0.24f
    Mood.STRESSED -> 0.24f
    Mood.SAD -> 0.18f
}

fun moodColorHex(mood: Mood): String = when (mood) {
    Mood.HAPPY -> "#E0A21A"
    Mood.GRATEFUL -> "#C77FA8"
    Mood.CALM -> "#2FA876"
    Mood.LOVED -> "#D9534A"
    Mood.HOPEFUL -> "#3F9C8D"
    Mood.PROUD -> "#F2932B"
    Mood.INSPIRED -> "#8A6FBF"
    Mood.REFLECTIVE -> "#6E7AD0"
    Mood.FOCUSED -> "#5E9FD6"
    Mood.NOSTALGIC -> "#B0815B"
    Mood.TIRED -> "#6E6C9E"
    Mood.ANXIOUS -> "#C08A1E"
    Mood.STRESSED -> "#CF6F64"
    Mood.FRUSTRATED -> "#C5453B"
    Mood.SAD -> "#5E72C8"
    Mood.MELANCHOLIC -> "#9CA3AF"
    Mood.BORED -> "#8E949B"
}
