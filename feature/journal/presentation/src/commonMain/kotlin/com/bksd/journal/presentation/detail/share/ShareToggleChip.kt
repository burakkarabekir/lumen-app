package com.bksd.journal.presentation.detail.share

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.PreviewAppTheme
import com.bksd.core.design_system.theme.dimens

@Composable
fun ShareToggleChip(
    label: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val primary = MaterialTheme.colorScheme.primary
    val onSurface = MaterialTheme.colorScheme.onSurface
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
        modifier = modifier
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.cardTight))
            .background(if (checked) primary.copy(alpha = 0.10f) else Color.Transparent)
            .border(
                width = 1.5.dp,
                color = if (checked) primary.copy(alpha = 0.55f) else onSurface.copy(alpha = 0.14f),
                shape = RoundedCornerShape(MaterialTheme.dimens.radius.cardTight)
            )
            .clickable { onToggle(!checked) }
            .padding(horizontal = MaterialTheme.dimens.spacing.lg, vertical = MaterialTheme.dimens.spacing.md)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(MaterialTheme.dimens.icon.md)
                .clip(RoundedCornerShape(MaterialTheme.dimens.radius.xs))
                .background(if (checked) primary else Color.Transparent)
                .then(
                    if (checked) Modifier else Modifier.border(1.5.dp, onSurface.copy(alpha = 0.3f), RoundedCornerShape(MaterialTheme.dimens.radius.xs))
                )
        ) {
            if (checked) {
                Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(MaterialTheme.dimens.icon.xs))
            }
        }
        Text(text = label, fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold, color = onSurface)
    }
}

@Preview
@Composable
private fun ShareToggleChipPreview() {
    PreviewAppTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.md), modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg)) {
            ShareToggleChip("Date", checked = true, onToggle = {})
            ShareToggleChip("Mood", checked = false, onToggle = {})
        }
    }
}
