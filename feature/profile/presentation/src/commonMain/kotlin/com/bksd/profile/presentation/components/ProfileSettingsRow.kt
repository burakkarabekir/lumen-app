package com.bksd.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.profileAccentIndigo

@Composable
fun ProfileSettingsRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accent: Color = MaterialTheme.colorScheme.extended.profileAccentIndigo,
    trailingValue: String? = null,
    trailingColor: Color? = null,
    showBadge: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = MaterialTheme.dimens.spacing.lg, vertical = MaterialTheme.dimens.spacing.lg),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(MaterialTheme.dimens.icon.avatar)
                .clip(RoundedCornerShape(MaterialTheme.dimens.radius.md))
                .background(accent.copy(alpha = 0.16f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = accent,
                modifier = Modifier.size(MaterialTheme.dimens.icon.md)
            )
        }
        Spacer(modifier = Modifier.width(MaterialTheme.dimens.spacing.md))
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        if (trailingValue != null) {
            Text(
                text = trailingValue,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = trailingColor ?: MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.width(MaterialTheme.dimens.spacing.sm))
        }

        if (showBadge) {
            Box(
                modifier = Modifier
                    .size(MaterialTheme.dimens.icon.xs)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.error)
            )
            Spacer(modifier = Modifier.width(MaterialTheme.dimens.spacing.sm))
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            modifier = Modifier.size(MaterialTheme.dimens.icon.md)
        )
    }
}

@Preview
@Composable
private fun ProfileSettingsRowDarkPreview() {
    AppTheme(darkTheme = true) {
        ProfileSettingsRow(
            icon = Icons.Default.Notifications,
            label = "Reminders",
            trailingValue = "9:00 PM",
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun ProfileSettingsRowLightPreview() {
    AppTheme(darkTheme = false) {
        ProfileSettingsRow(
            icon = Icons.Default.Notifications,
            label = "Reminders",
            trailingValue = "9:00 PM",
            onClick = {}
        )
    }
}
