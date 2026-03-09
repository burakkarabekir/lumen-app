package com.bksd.profile.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.PreviewAppTheme
import com.bksd.core.design_system.theme.rememberThemeController
import com.bksd.core.domain.theme.AppThemeMode
import com.bksd.profile.presentation.Res
import com.bksd.profile.presentation.theme_dark
import com.bksd.profile.presentation.theme_light
import com.bksd.profile.presentation.theme_selector_title
import com.bksd.profile.presentation.theme_system
import org.jetbrains.compose.resources.stringResource

/**
 * Bottom sheet for theme selection.
 * Accesses [ThemeController] via CompositionLocal — no ViewModel involved.
 * Calls [ThemeController.setTheme] directly on selection.
 */
@Composable
fun ThemeSelectorSheet(
    onDismiss: () -> Unit,
) {
    val themeController = rememberThemeController()
    val currentMode by themeController.themeMode.collectAsState()

    ThemeSelectorSheetContent(
        currentMode = currentMode,
        onThemeSelected = { mode ->
            themeController.setTheme(mode)
            onDismiss()
        },
        onDismiss = onDismiss,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ThemeSelectorSheetContent(
    currentMode: AppThemeMode,
    onThemeSelected: (AppThemeMode) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 48.dp)
        ) {
            Text(
                text = stringResource(Res.string.theme_selector_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            ThemeOptionRow(
                label = stringResource(Res.string.theme_system),
                isSelected = currentMode == AppThemeMode.SYSTEM,
                onClick = { onThemeSelected(AppThemeMode.SYSTEM) },
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
            )
            ThemeOptionRow(
                label = stringResource(Res.string.theme_light),
                isSelected = currentMode == AppThemeMode.LIGHT,
                onClick = { onThemeSelected(AppThemeMode.LIGHT) },
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
            )
            ThemeOptionRow(
                label = stringResource(Res.string.theme_dark),
                isSelected = currentMode == AppThemeMode.DARK,
                onClick = { onThemeSelected(AppThemeMode.DARK) },
            )
        }
    }
}

@Composable
private fun ThemeOptionRow(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier.weight(1f),
        )

        if (isSelected) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

// ==================== Previews ====================

@Preview
@Composable
private fun ThemeSelectorSheetDarkPreview() {
    PreviewAppTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            ThemeOptionRow(label = "System", isSelected = false, onClick = {})
            ThemeOptionRow(label = "Light", isSelected = false, onClick = {})
            ThemeOptionRow(label = "Dark", isSelected = true, onClick = {})
        }
    }
}

@Preview
@Composable
private fun ThemeSelectorSheetLightPreview() {
    PreviewAppTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            ThemeOptionRow(label = "System", isSelected = true, onClick = {})
            ThemeOptionRow(label = "Light", isSelected = false, onClick = {})
            ThemeOptionRow(label = "Dark", isSelected = false, onClick = {})
        }
    }
}
