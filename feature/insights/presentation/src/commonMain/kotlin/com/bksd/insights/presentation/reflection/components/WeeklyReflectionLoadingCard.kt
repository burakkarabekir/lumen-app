package com.bksd.insights.presentation.reflection.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.insights.presentation.Res
import com.bksd.insights.presentation.weekly_reflecting
import org.jetbrains.compose.resources.stringResource

@Composable
fun WeeklyReflectionLoadingCard(modifier: Modifier = Modifier) {
    val c = MaterialTheme.colorScheme.extended.reflectionCard

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.card))
            .background(Brush.linearGradient(c.loadingSurfaceGradient))
            .border(1.dp, c.loadingBorder, RoundedCornerShape(MaterialTheme.dimens.radius.card))
            .padding(MaterialTheme.dimens.spacing.xl)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(MaterialTheme.dimens.icon.tile)
                .clip(RoundedCornerShape(MaterialTheme.dimens.radius.md))
                .background(Brush.linearGradient(listOf(c.iconStart, c.iconEnd)))
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(MaterialTheme.dimens.icon.lg)
            )
        }
        Spacer(Modifier.width(MaterialTheme.dimens.spacing.md))
        Text(
            text = stringResource(Res.string.weekly_reflecting),
            fontSize = 14.5.sp,
            fontWeight = FontWeight.ExtraBold,
            color = c.title,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(MaterialTheme.dimens.spacing.md))
        CircularProgressIndicator(
            modifier = Modifier.size(MaterialTheme.dimens.icon.lg),
            strokeWidth = 2.dp,
            color = c.iconStart
        )
    }
}

@Preview
@Composable
private fun WeeklyReflectionLoadingCardPreview() {
    AppTheme {
        WeeklyReflectionLoadingCard(modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg))
    }
}
