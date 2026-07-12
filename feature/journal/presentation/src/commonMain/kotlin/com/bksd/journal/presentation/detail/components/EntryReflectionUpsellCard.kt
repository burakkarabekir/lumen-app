package com.bksd.journal.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.core.design_system.component.button.AppButton
import com.bksd.core.design_system.component.button.AppButtonStyle
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.aiIconGradient
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.journal.presentation.Res
import com.bksd.journal.presentation.upsell_reflections_body
import com.bksd.journal.presentation.upsell_reflections_title
import com.bksd.journal.presentation.upsell_unlock_plus
import org.jetbrains.compose.resources.stringResource

@Composable
fun EntryReflectionUpsellCard(
    onUnlock: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.card))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(MaterialTheme.dimens.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.md),
    ) {
        Box(
            modifier = Modifier
                .size(MaterialTheme.dimens.icon.avatar)
                .clip(CircleShape)
                .background(Brush.linearGradient(MaterialTheme.colorScheme.extended.aiIconGradient)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.AutoAwesome,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(MaterialTheme.dimens.icon.lg),
            )
        }
        Text(
            text = stringResource(Res.string.upsell_reflections_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = stringResource(Res.string.upsell_reflections_body),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        AppButton(
            text = stringResource(Res.string.upsell_unlock_plus),
            onClick = onUnlock,
            style = AppButtonStyle.PRIMARY,
            cornerRadius = MaterialTheme.dimens.radius.lg,
        )
    }
}

@Preview
@Composable
private fun EntryReflectionUpsellCardPreview() {
    AppTheme {
        EntryReflectionUpsellCard(onUnlock = {})
    }
}
