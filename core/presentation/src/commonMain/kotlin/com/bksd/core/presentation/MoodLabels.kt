package com.bksd.core.presentation

import com.bksd.core.domain.model.Mood
import org.jetbrains.compose.resources.StringResource

fun Mood.labelRes(): StringResource = when (this) {
    Mood.HAPPY -> Res.string.mood_happy
    Mood.GRATEFUL -> Res.string.mood_grateful
    Mood.CALM -> Res.string.mood_calm
    Mood.LOVED -> Res.string.mood_loved
    Mood.HOPEFUL -> Res.string.mood_hopeful
    Mood.PROUD -> Res.string.mood_proud
    Mood.INSPIRED -> Res.string.mood_inspired
    Mood.REFLECTIVE -> Res.string.mood_reflective
    Mood.FOCUSED -> Res.string.mood_focused
    Mood.NOSTALGIC -> Res.string.mood_nostalgic
    Mood.TIRED -> Res.string.mood_tired
    Mood.ANXIOUS -> Res.string.mood_anxious
    Mood.STRESSED -> Res.string.mood_stressed
    Mood.FRUSTRATED -> Res.string.mood_frustrated
    Mood.SAD -> Res.string.mood_sad
    Mood.MELANCHOLIC -> Res.string.mood_melancholic
    Mood.BORED -> Res.string.mood_bored
}
