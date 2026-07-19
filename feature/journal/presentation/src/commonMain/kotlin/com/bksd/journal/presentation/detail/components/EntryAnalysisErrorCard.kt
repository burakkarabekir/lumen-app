package com.bksd.journal.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
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
import com.bksd.core.design_system.component.button.AppButton
import com.bksd.core.design_system.component.button.AppButtonStyle
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
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
                .background(MaterialTheme.colorScheme.error),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = if (isOffline) Icons.Filled.CloudOff else Icons.Filled.ErrorOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onError,
                modifier = Modifier.size(MaterialTheme.dimens.icon.xl),
            )
        }
        Text(
            text = stringResource(
                if (isOffline) Res.string.ai_analysis_offline_title else Res.string.ai_analysis_failed_title,
            ),
            fontSize = 20.sp,
            lineHeight = 25.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = (-0.3).sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.lg),
        )
        Text(
            text = stringResource(
                if (isOffline) Res.string.ai_analysis_offline_caption else Res.string.ai_analysis_failed_caption,
            ),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = MaterialTheme.dimens.spacing.sm),
        )
        Spacer(Modifier.height(MaterialTheme.dimens.spacing.xl))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            AppButton(
                text = stringResource(Res.string.ai_analysis_retry),
                onClick = onRetry,
                style = AppButtonStyle.SECONDARY,
                cornerRadius = MaterialTheme.dimens.radius.full,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(MaterialTheme.dimens.icon.sm),
                    )
                },
            )
        }
    }
}

@Preview
@Composable
private fun EntryAnalysisErrorCardPreview() {
    AppTheme {
        EntryAnalysisErrorCard(
            onRetry = {},
            modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg),
        )
    }
}

@Preview
@Composable
private fun EntryAnalysisErrorCardOfflinePreview() {
    AppTheme {
        EntryAnalysisErrorCard(
            onRetry = {},
            isOffline = true,
            modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg),
        )
    }
}

@Preview
@Composable
private fun EntryAnalysisErrorCardDarkPreview() {
    AppTheme(darkTheme = true) {
        EntryAnalysisErrorCard(
            onRetry = {},
            modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg),
        )
    }
}
