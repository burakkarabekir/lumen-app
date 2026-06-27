package com.bksd.moment.presentation.create.components

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
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
    val visual = moodVisual(mood)
    val background = if (isSelected) visual.hue else visual.hue.copy(alpha = 0.18f)
    val contentColor = if (isSelected) Color.White else palette.text
    val iconTint = if (isSelected) Color.White else visual.hue

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(background)
            .clickable(onClick = onClick)
            .padding(start = 12.dp, end = 14.dp, top = 9.dp, bottom = 9.dp)
    ) {
        Icon(
            imageVector = visual.icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = mood.label,
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
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .background(palette.pageBg)
                .padding(16.dp)
        ) {
            MoodChip(mood = Mood.CALM, isSelected = true, onClick = {})
            MoodChip(mood = Mood.GRATEFUL, isSelected = false, onClick = {})
        }
    }
}
