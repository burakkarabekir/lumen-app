package com.bksd.journal.presentation.journal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.extended
import com.bksd.journal.domain.model.Mood

@Composable
fun MoodTag(mood: Mood) {
    val extendedColors = MaterialTheme.colorScheme.extended
    val (backgroundColor, textColor) = when (mood) {
        Mood.ENERGETIC -> extendedColors.emotionJoyBg to extendedColors.emotionJoy
        Mood.REFLECTIVE -> extendedColors.emotionSurpriseBg to extendedColors.emotionSurprise
        Mood.CALM -> extendedColors.emotionCalmBg to extendedColors.emotionCalm
        Mood.TIRED -> extendedColors.emotionSadnessBg to extendedColors.emotionSadness
        Mood.INSPIRED -> extendedColors.emotionGratitudeBg to extendedColors.emotionGratitude
    }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = mood.emoji, fontSize = 12.sp)
        Text(
            text = mood.label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Preview
@Composable
fun PreviewMoodTag() {
    AppTheme {
        MoodTag(
            Mood.REFLECTIVE
        )
    }
}
