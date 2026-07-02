package com.bksd.journal.presentation.detail.share

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.bksd.core.design_system.theme.PreviewAppTheme
import com.bksd.core.design_system.theme.dimens

@Composable
fun ShareFormatSelector(
    selected: ShareFormat,
    onSelect: (ShareFormat) -> Unit,
    modifier: Modifier = Modifier,
) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    Row(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
        modifier = modifier
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.lg))
            .background(onSurface.copy(alpha = 0.06f))
            .padding(MaterialTheme.dimens.spacing.xs)
    ) {
        ShareFormat.entries.forEach { format ->
            val active = format == selected
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(MaterialTheme.dimens.radius.md))
                    .background(if (active) onSurface.copy(alpha = 0.12f) else androidx.compose.ui.graphics.Color.Transparent)
                    .clickable { onSelect(format) }
                    .padding(vertical = MaterialTheme.dimens.spacing.md)
            ) {
                Box(
                    modifier = Modifier
                        .height(MaterialTheme.dimens.icon.sm)
                        .width(MaterialTheme.dimens.icon.sm * format.ratio)
                        .clip(RoundedCornerShape(MaterialTheme.dimens.radius.xs))
                        .border(
                            width = 1.5.dp,
                            color = if (active) onSurface else onSurface.copy(alpha = 0.45f),
                            shape = RoundedCornerShape(MaterialTheme.dimens.radius.xs)
                        )
                )
                Spacer(Modifier.width(MaterialTheme.dimens.spacing.sm))
                Text(
                    text = format.label,
                    fontSize = 13.sp,
                    fontWeight = if (active) FontWeight.Bold else FontWeight.Medium,
                    color = if (active) onSurface else onSurface.copy(alpha = 0.55f)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ShareFormatSelectorPreview() {
    PreviewAppTheme {
        ShareFormatSelector(
            selected = ShareFormat.PORTRAIT,
            onSelect = {},
            modifier = Modifier.fillMaxWidth().padding(MaterialTheme.dimens.spacing.lg)
        )
    }
}
