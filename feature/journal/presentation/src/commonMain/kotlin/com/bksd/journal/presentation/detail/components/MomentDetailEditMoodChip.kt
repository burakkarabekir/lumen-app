package com.bksd.journal.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.domain.model.Mood
import com.bksd.journal.presentation.journal.components.moodColors

@Composable
fun MomentDetailEditMoodChip(
    mood: Mood,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = moodColors(mood, MaterialTheme.colorScheme.extended).second
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
        modifier = modifier
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.lg))
            .background(accent)
            .padding(
                start = MaterialTheme.dimens.spacing.md,
                end = MaterialTheme.dimens.spacing.sm,
                top = MaterialTheme.dimens.spacing.sm,
                bottom = MaterialTheme.dimens.spacing.sm
            )
    ) {
        Icon(
            imageVector = detailMoodIcon(mood),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(MaterialTheme.dimens.icon.sm)
        )
        Text(
            text = mood.label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(MaterialTheme.dimens.icon.md)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.25f))
                .clickable(onClick = onRemove)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(MaterialTheme.dimens.icon.xs)
            )
        }
    }
}

@Preview
@Composable
private fun MomentDetailEditMoodChipPreview() {
    AppTheme(darkTheme = true) {
        Row(modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg)) {
            MomentDetailEditMoodChip(mood = Mood.CALM, onRemove = {})
        }
    }
}
