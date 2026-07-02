package com.bksd.core.design_system.theme

import androidx.compose.ui.graphics.Color
import com.bksd.core.domain.model.Mood

/** Solid brand hue used for a mood's icon / accent dot. Theme-independent. */
fun ExtendedColors.moodHue(mood: Mood): Color = when (mood) {
    Mood.HAPPY -> MoodHueHappy
    Mood.GRATEFUL -> MoodHueGrateful
    Mood.CALM -> MoodHueCalm
    Mood.LOVED -> MoodHueLoved
    Mood.HOPEFUL -> MoodHueHopeful
    Mood.PROUD -> MoodHueProud
    Mood.INSPIRED -> MoodHueInspired
    Mood.REFLECTIVE -> MoodHueReflective
    Mood.FOCUSED -> MoodHueFocused
    Mood.NOSTALGIC -> MoodHueNostalgic
    Mood.TIRED -> MoodHueTired
    Mood.ANXIOUS -> MoodHueAnxious
    Mood.STRESSED -> MoodHueStressed
    Mood.FRUSTRATED -> MoodHueFrustrated
    Mood.SAD -> MoodHueSad
    Mood.MELANCHOLIC -> MoodHueMelancholic
    Mood.BORED -> MoodHueBored
}

/** (background, foreground) pair for a mood chip. Reuses emotion tokens where they exist. */
fun ExtendedColors.moodChip(mood: Mood): Pair<Color, Color> = when (mood) {
    Mood.HAPPY -> emotionJoyBg to emotionJoy
    Mood.PROUD -> MoodChipProud.copy(alpha = 0.15f) to MoodChipProud
    Mood.GRATEFUL -> emotionGratitudeBg to emotionGratitude
    Mood.CALM -> emotionCalmBg to emotionCalm
    Mood.HOPEFUL -> MoodChipHopeful.copy(alpha = 0.15f) to MoodChipHopeful
    Mood.REFLECTIVE -> MoodChipReflective.copy(alpha = 0.15f) to MoodChipReflective
    Mood.INSPIRED -> MoodChipInspired.copy(alpha = 0.15f) to MoodChipInspired
    Mood.FOCUSED -> MoodChipFocused.copy(alpha = 0.15f) to MoodChipFocused
    Mood.LOVED -> MoodChipLoved.copy(alpha = 0.15f) to MoodChipLoved
    Mood.NOSTALGIC -> MoodChipNostalgic.copy(alpha = 0.15f) to MoodChipNostalgic
    Mood.TIRED -> emotionSadnessBg to emotionSadness
    Mood.ANXIOUS -> emotionAngerBg to emotionAnger
    Mood.STRESSED -> MoodChipStressed.copy(alpha = 0.15f) to MoodChipStressed
    Mood.FRUSTRATED -> MoodChipFrustrated.copy(alpha = 0.15f) to MoodChipFrustrated
    Mood.SAD -> MoodChipSad.copy(alpha = 0.15f) to MoodChipSad
    Mood.MELANCHOLIC -> MoodChipMelancholic.copy(alpha = 0.15f) to MoodChipMelancholic
    Mood.BORED -> MoodChipBored.copy(alpha = 0.15f) to MoodChipBored
}
