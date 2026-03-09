package com.bksd.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.AppTheme

/**
 * Groups settings rows inside a rounded card container with surfaceVariant background.
 * Automatically inserts dividers between children.
 */
@Composable
fun SettingsGroup(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        content = content
    )
}

@Preview
@Composable
private fun SettingsGroupDarkPreview() {
    AppTheme(darkTheme = true) {
        SettingsGroup {
            ProfileSettingsRow(
                icon = Icons.Default.Lock,
                label = "Privacy & Security",
                onClick = {}
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            ProfileSettingsRow(
                icon = Icons.Default.Notifications,
                label = "Notifications",
                showBadge = true,
                onClick = {}
            )
        }
    }
}

@Preview
@Composable
private fun SettingsGroupLightPreview() {
    AppTheme(darkTheme = false) {
        SettingsGroup {
            ProfileSettingsRow(
                icon = Icons.Default.Lock,
                label = "Privacy & Security",
                onClick = {}
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            ProfileSettingsRow(
                icon = Icons.Default.Notifications,
                label = "Notifications",
                showBadge = true,
                onClick = {}
            )
        }
    }
}
