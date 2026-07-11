package com.bksd.profile.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.theme.PreviewAppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.rememberThemeController
import com.bksd.core.domain.theme.AppThemeMode
import com.bksd.profile.presentation.Res
import com.bksd.profile.presentation.appearance_auto
import com.bksd.profile.presentation.appearance_dark
import com.bksd.profile.presentation.appearance_light
import com.bksd.profile.presentation.theme_selector_title
import org.jetbrains.compose.resources.stringResource

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
                .padding(horizontal = MaterialTheme.dimens.spacing.lg)
                .padding(bottom = MaterialTheme.dimens.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.xs),
        ) {
            Text(
                text = stringResource(Res.string.theme_selector_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(
                    start = MaterialTheme.dimens.spacing.md,
                    top = MaterialTheme.dimens.spacing.sm,
                    bottom = MaterialTheme.dimens.spacing.md,
                ),
            )

            ThemeOptionRow(
                icon = Icons.Default.BrightnessAuto,
                label = stringResource(Res.string.appearance_auto),
                isSelected = currentMode == AppThemeMode.SYSTEM,
                onClick = { onThemeSelected(AppThemeMode.SYSTEM) },
            )
            ThemeOptionRow(
                icon = Icons.Default.LightMode,
                label = stringResource(Res.string.appearance_light),
                isSelected = currentMode == AppThemeMode.LIGHT,
                onClick = { onThemeSelected(AppThemeMode.LIGHT) },
            )
            ThemeOptionRow(
                icon = Icons.Default.DarkMode,
                label = stringResource(Res.string.appearance_dark),
                isSelected = currentMode == AppThemeMode.DARK,
                onClick = { onThemeSelected(AppThemeMode.DARK) },
            )
        }
    }
}

@Preview
@Composable
private fun ThemeSelectorSheetContentPreview() {
    PreviewAppTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.xs),
        ) {
            ThemeOptionRow(
                icon = Icons.Default.BrightnessAuto,
                label = "Auto",
                isSelected = true,
                onClick = {},
            )
            ThemeOptionRow(
                icon = Icons.Default.LightMode,
                label = "Light",
                isSelected = false,
                onClick = {},
            )
            ThemeOptionRow(
                icon = Icons.Default.DarkMode,
                label = "Dark",
                isSelected = false,
                onClick = {},
            )
        }
    }
}
