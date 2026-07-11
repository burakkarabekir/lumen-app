package com.bksd.journal.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
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
import com.bksd.core.design_system.component.button.AppButton
import com.bksd.core.design_system.component.button.AppButtonStyle
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.ai_analysis_failed_caption
import com.bksd.journal.presentation.ai_analysis_failed_title
import com.bksd.journal.presentation.ai_analysis_offline_caption
import com.bksd.journal.presentation.ai_analysis_offline_title
import com.bksd.journal.presentation.ai_analysis_retry
import org.jetbrains.compose.resources.stringResource

@Composable
fun EntryAnalysisErrorCard(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    isOffline: Boolean = false,
) {
    val c = MaterialTheme.colorScheme.extended.reflectionCard

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.card))
            .background(Brush.linearGradient(c.loadingSurfaceGradient))
            .border(1.dp, c.loadingBorder, RoundedCornerShape(MaterialTheme.dimens.radius.card))
            .padding(MaterialTheme.dimens.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.lg)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(MaterialTheme.dimens.icon.tile)
                    .clip(RoundedCornerShape(MaterialTheme.dimens.radius.md))
                    .background(MaterialTheme.colorScheme.error.copy(alpha = 0.12f))
            ) {
                Icon(
                    imageVector = if (isOffline) Icons.Default.CloudOff else Icons.Default.ErrorOutline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(MaterialTheme.dimens.icon.lg)
                )
            }
            Spacer(Modifier.width(MaterialTheme.dimens.spacing.md))
            Column(Modifier.weight(1f)) {
                Text(
                    text = stringResource(if (isOffline) Res.string.ai_analysis_offline_title else Res.string.ai_analysis_failed_title),
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = c.title
                )
                Spacer(Modifier.size(MaterialTheme.dimens.spacing.xxs))
                Text(
                    text = stringResource(if (isOffline) Res.string.ai_analysis_offline_caption else Res.string.ai_analysis_failed_caption),
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = c.meta
                )
            }
        }
        AppButton(
            text = stringResource(Res.string.ai_analysis_retry),
            onClick = onRetry,
            style = AppButtonStyle.SECONDARY,
            cornerRadius = MaterialTheme.dimens.radius.lg,
        )
    }
}

@Preview
@Composable
private fun EntryAnalysisErrorCardPreview() {
    AppTheme {
        EntryAnalysisErrorCard(
            onRetry = {},
            modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg)
        )
    }
}

@Preview
@Composable
private fun EntryAnalysisErrorCardDarkPreview() {
    AppTheme(darkTheme = true) {
        EntryAnalysisErrorCard(
            onRetry = {},
            modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg)
        )
    }
}
