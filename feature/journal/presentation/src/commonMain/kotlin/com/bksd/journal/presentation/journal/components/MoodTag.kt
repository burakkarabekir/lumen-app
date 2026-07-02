package com.bksd.journal.presentation.journal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.domain.model.Mood

@Composable
fun MoodTag(
    mood: Mood,
    modifier: Modifier = Modifier,
    alphaProvider: () -> Float = { 1f }
) {
    val extendedColors = MaterialTheme.colorScheme.extended
    val (backgroundColor, textColor) = remember(mood) {
        moodColors(mood, extendedColors)
    }

    Row(
        modifier = modifier
            .graphicsLayer {
                alpha = alphaProvider()
            }
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.sm))
            .background(backgroundColor)
            .padding(horizontal = MaterialTheme.dimens.spacing.sm, vertical = MaterialTheme.dimens.spacing.xs),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.xs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = mood.emoji, style = MaterialTheme.typography.labelSmall)
        Text(
            text = mood.label,
            style = MaterialTheme.typography.labelSmall,
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
