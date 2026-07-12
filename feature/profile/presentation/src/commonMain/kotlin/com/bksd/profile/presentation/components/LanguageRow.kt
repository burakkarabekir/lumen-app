package com.bksd.profile.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.domain.language.AppLanguage
import com.bksd.profile.presentation.Res
import com.bksd.profile.presentation.language_system
import com.bksd.profile.presentation.language_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun LanguageRow(
    selectedLanguage: AppLanguage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val value = when (selectedLanguage) {
        AppLanguage.SYSTEM -> stringResource(Res.string.language_system)
        AppLanguage.ENGLISH -> "English"
        AppLanguage.TURKISH -> "Türkçe"
        AppLanguage.GERMAN -> "Deutsch"
        AppLanguage.SPANISH -> "Español"
        AppLanguage.FRENCH -> "Français"
    }
    ProfileSettingsRow(
        icon = Icons.Default.Language,
        label = stringResource(Res.string.language_title),
        trailingValue = value,
        onClick = onClick,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun LanguageRowPreview() {
    AppTheme(darkTheme = true) {
        LanguageRow(selectedLanguage = AppLanguage.ENGLISH, onClick = {})
    }
}
