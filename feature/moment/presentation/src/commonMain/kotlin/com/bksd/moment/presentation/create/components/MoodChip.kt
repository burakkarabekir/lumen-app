package com.bksd.moment.presentation.create.components
import com.bksd.core.presentation.labelRes
import org.jetbrains.compose.resources.stringResource

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.moodHue
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.core.domain.model.Mood

@Composable
fun MoodChip(
    mood: Mood,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = rememberNewEntryPalette()
    val hue = MaterialTheme.colorScheme.extended.moodHue(mood)
    val background = if (isSelected) hue else hue.copy(alpha = 0.18f)
    val contentColor = if (isSelected) Color.White else palette.text
    val iconTint = if (isSelected) Color.White else hue

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
        modifier = modifier
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.lg))
            .background(background)
            .clickable(onClick = onClick)
            .padding(start = MaterialTheme.dimens.spacing.md, end = MaterialTheme.dimens.spacing.lg, top = MaterialTheme.dimens.spacing.sm, bottom = MaterialTheme.dimens.spacing.sm)
    ) {
        Icon(
            imageVector = moodIcon(mood),
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(MaterialTheme.dimens.icon.sm)
        )
        Text(
            text = stringResource(mood.labelRes()),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = contentColor
        )
    }
}

@Preview
@Composable
private fun MoodChipPreview() {
    AppTheme(darkTheme = true) {
        val palette = rememberNewEntryPalette()
        Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
            modifier = Modifier
                .background(palette.pageBg)
                .padding(MaterialTheme.dimens.spacing.lg)
        ) {
            MoodChip(mood = Mood.CALM, isSelected = true, onClick = {})
            MoodChip(mood = Mood.GRATEFUL, isSelected = false, onClick = {})
        }
    }
}
