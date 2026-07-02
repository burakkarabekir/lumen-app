package com.bksd.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
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
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.profileAccentIndigo
import com.bksd.profile.presentation.Res
import com.bksd.profile.presentation.appearance_dark
import com.bksd.profile.presentation.appearance_light
import com.bksd.profile.presentation.appearance_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppearanceRow(
    isDark: Boolean,
    onSelectLight: () -> Unit,
    onSelectDark: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accent = MaterialTheme.colorScheme.extended.profileAccentIndigo
    Row(
        modifier = modifier
            .fillMaxWidth()
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
                imageVector = Icons.Default.DarkMode,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(MaterialTheme.dimens.icon.md)
            )
        }
        Spacer(Modifier.width(MaterialTheme.dimens.spacing.md))
        Text(
            text = stringResource(Res.string.appearance_title),
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(MaterialTheme.dimens.radius.md))
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                .padding(MaterialTheme.dimens.spacing.xs),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(MaterialTheme.dimens.radius.sm))
                    .background(if (!isDark) MaterialTheme.colorScheme.surface else Color.Transparent)
                    .clickable(onClick = onSelectLight)
                    .padding(horizontal = MaterialTheme.dimens.spacing.md, vertical = MaterialTheme.dimens.spacing.xs)
            ) {
                Text(
                    text = stringResource(Res.string.appearance_light),
                    fontSize = 11.5.sp,
                    fontWeight = if (!isDark) FontWeight.Bold else FontWeight.Medium,
                    color = if (!isDark) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    }
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(MaterialTheme.dimens.radius.sm))
                    .background(if (isDark) MaterialTheme.colorScheme.surface else Color.Transparent)
                    .clickable(onClick = onSelectDark)
                    .padding(horizontal = MaterialTheme.dimens.spacing.md, vertical = MaterialTheme.dimens.spacing.xs)
            ) {
                Text(
                    text = stringResource(Res.string.appearance_dark),
                    fontSize = 11.5.sp,
                    fontWeight = if (isDark) FontWeight.Bold else FontWeight.Medium,
                    color = if (isDark) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun AppearanceRowPreview() {
    AppTheme(darkTheme = true) {
        AppearanceRow(isDark = false, onSelectLight = {}, onSelectDark = {})
    }
}
