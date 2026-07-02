package com.bksd.journal.presentation.detail.share

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.PreviewAppTheme
import com.bksd.core.design_system.theme.ShareStyle
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.shareCardColors

@Composable
fun ShareStyleSwatch(
    style: ShareStyle,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = shareCardColors(style)
    val primary = MaterialTheme.colorScheme.primary
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.clickable(onClick = onClick)) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .size(MaterialTheme.dimens.size.topBar)
                .clip(RoundedCornerShape(MaterialTheme.dimens.radius.xl))
                .background(Brush.linearGradient(listOf(colors.backgroundTop, colors.backgroundBottom)))
                .then(
                    if (selected) Modifier.border(2.5.dp, primary, RoundedCornerShape(MaterialTheme.dimens.radius.xl)) else Modifier
                )
        )
        Spacer(Modifier.height(MaterialTheme.dimens.spacing.sm))
        Text(
            text = label,
            fontSize = 12.5.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
        )
    }
}

@Preview
@Composable
private fun ShareStyleSwatchPreview() {
    PreviewAppTheme {
        androidx.compose.foundation.layout.Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.lg),
            modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg)
        ) {
            ShareStyleSwatch(ShareStyle.AURORA, "Aurora", selected = true, onClick = {})
            ShareStyleSwatch(ShareStyle.PAPER, "Paper", selected = false, onClick = {})
            ShareStyleSwatch(ShareStyle.INK, "Ink", selected = false, onClick = {})
        }
    }
}
