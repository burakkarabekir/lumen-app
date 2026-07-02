package com.bksd.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.profile.presentation.Res
import com.bksd.profile.presentation.momentum_pro_description
import com.bksd.profile.presentation.momentum_pro_title
import com.bksd.profile.presentation.upgrade_button
import org.jetbrains.compose.resources.stringResource

/**
 * Momentum Pro upgrade promotion card matching the Stitch design.
 */
@Composable
fun MomentumProCard(
    onUpgradeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MaterialTheme.dimens.radius.lg))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(MaterialTheme.dimens.spacing.lg),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(MaterialTheme.dimens.icon.xl)
        )
        Spacer(modifier = Modifier.width(MaterialTheme.dimens.spacing.md))

        // Copy
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(Res.string.momentum_pro_title),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.spacing.xs))
            Text(
                text = stringResource(Res.string.momentum_pro_description),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                lineHeight = 18.sp
            )
        }

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.spacing.md))

        // Upgrade button
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(MaterialTheme.dimens.radius.xl))
                .background(MaterialTheme.colorScheme.primary)
                .clickable { onUpgradeClick() }
                .padding(horizontal = MaterialTheme.dimens.spacing.lg, vertical = MaterialTheme.dimens.spacing.sm)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.xs)
            ) {
                Text(
                    text = stringResource(Res.string.upgrade_button),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(MaterialTheme.dimens.icon.xs)
                )
            }
        }
    }
}

@Preview
@Composable
private fun MomentumProCardDarkPreview() {
    AppTheme(darkTheme = true) {
        MomentumProCard(onUpgradeClick = {})
    }
}

@Preview
@Composable
private fun MomentumProCardLightPreview() {
    AppTheme(darkTheme = false) {
        MomentumProCard(onUpgradeClick = {})
    }
}
