package com.bksd.journal.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
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
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.daily_limit_reflections_body
import com.bksd.journal.presentation.daily_limit_reflections_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun EntryDailyLimitCard(
    modifier: Modifier = Modifier,
) {
    val c = MaterialTheme.colorScheme.extended.reflectionCard

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.card))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .border(
                1.dp,
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f),
                RoundedCornerShape(MaterialTheme.dimens.radius.card),
            )
            .padding(MaterialTheme.dimens.spacing.xl),
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.linearGradient(listOf(c.iconStart, c.iconEnd))),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Schedule,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(MaterialTheme.dimens.icon.xl),
            )
        }
        Text(
            text = stringResource(Res.string.daily_limit_reflections_title),
            fontSize = 20.sp,
            lineHeight = 25.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = (-0.3).sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.lg),
        )
        Text(
            text = stringResource(Res.string.daily_limit_reflections_body),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.sm),
        )
    }
}

@Preview
@Composable
private fun EntryDailyLimitCardPreview() {
    AppTheme {
        EntryDailyLimitCard(modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg))
    }
}

@Preview
@Composable
private fun EntryDailyLimitCardDarkPreview() {
    AppTheme(darkTheme = true) {
        EntryDailyLimitCard(modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg))
    }
}
