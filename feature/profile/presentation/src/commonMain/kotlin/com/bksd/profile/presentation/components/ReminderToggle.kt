package com.bksd.profile.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.rememberNewEntryPalette

@Composable
fun ReminderToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = rememberNewEntryPalette()
    val trackColor by animateColorAsState(
        targetValue = if (checked) palette.saveBg else lerp(palette.surface, palette.text, 0.18f),
        animationSpec = tween(200),
        label = "track"
    )
    val knobOffset by animateDpAsState(
        targetValue = if (checked) 20.dp else 0.dp,
        animationSpec = tween(200),
        label = "knob"
    )
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .size(width = 52.dp, height = 32.dp)
            .clip(CircleShape)
            .background(trackColor)
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .offset(x = knobOffset)
                .size(24.dp)
                .clip(CircleShape)
                .background(Color.White)
        )
    }
}

@Preview
@Composable
private fun ReminderTogglePreview() {
    AppTheme(darkTheme = true) {
        val palette = rememberNewEntryPalette()
        Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.lg),
            modifier = Modifier.background(palette.pageBg).padding(MaterialTheme.dimens.spacing.lg)
        ) {
            ReminderToggle(checked = true, onCheckedChange = {})
            ReminderToggle(checked = false, onCheckedChange = {})
        }
    }
}
