package com.bksd.journal.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import com.bksd.core.domain.model.Mood
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.journal.components.moodColors
import com.bksd.journal.presentation.mood_of_the_moment
import org.jetbrains.compose.resources.stringResource

@Composable
fun MomentDetailMoodBadge(
    mood: Mood,
    modifier: Modifier = Modifier
) {
    val extendedColors = MaterialTheme.colorScheme.extended
    val (bgColor, _) = moodColors(mood, extendedColors)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = mood.emoji,
                fontSize = 28.sp
            )
        }

        Text(
            text = mood.label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = stringResource(Res.string.mood_of_the_moment),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 1.sp
        )
    }
}

@Preview
@Composable
private fun MomentDetailMoodBadgePreview() {
    AppTheme {
        MomentDetailMoodBadge(mood = Mood.REFLECTIVE)
    }
}
