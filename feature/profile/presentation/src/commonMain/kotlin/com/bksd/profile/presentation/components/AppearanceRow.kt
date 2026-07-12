package com.bksd.profile.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.domain.theme.AppThemeMode
import com.bksd.profile.presentation.Res
import com.bksd.profile.presentation.appearance_auto
import com.bksd.profile.presentation.appearance_dark
import com.bksd.profile.presentation.appearance_light
import com.bksd.profile.presentation.appearance_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppearanceRow(
    selectedMode: AppThemeMode,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val valueRes = when (selectedMode) {
        AppThemeMode.SYSTEM -> Res.string.appearance_auto
        AppThemeMode.LIGHT -> Res.string.appearance_light
        AppThemeMode.DARK -> Res.string.appearance_dark
    }
    ProfileSettingsRow(
        icon = Icons.Default.DarkMode,
        label = stringResource(Res.string.appearance_title),
        trailingValue = stringResource(valueRes),
        onClick = onClick,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun AppearanceRowPreview() {
    AppTheme(darkTheme = true) {
        AppearanceRow(selectedMode = AppThemeMode.SYSTEM, onClick = {})
    }
}
