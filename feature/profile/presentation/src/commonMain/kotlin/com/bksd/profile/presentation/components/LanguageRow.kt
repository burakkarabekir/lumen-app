package com.bksd.profile.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.core.design_system.theme.AppTheme
import com.bksd.core.domain.language.AppLanguage
import com.bksd.profile.presentation.Res
import com.bksd.profile.presentation.language_name_english
import com.bksd.profile.presentation.language_name_french
import com.bksd.profile.presentation.language_name_german
import com.bksd.profile.presentation.language_name_spanish
import com.bksd.profile.presentation.language_name_turkish
import com.bksd.profile.presentation.language_system
import com.bksd.profile.presentation.language_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun LanguageRow(
    selectedLanguage: AppLanguage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val value = stringResource(
        when (selectedLanguage) {
            AppLanguage.SYSTEM -> Res.string.language_system
            AppLanguage.ENGLISH -> Res.string.language_name_english
            AppLanguage.TURKISH -> Res.string.language_name_turkish
            AppLanguage.GERMAN -> Res.string.language_name_german
            AppLanguage.SPANISH -> Res.string.language_name_spanish
            AppLanguage.FRENCH -> Res.string.language_name_french
        }
    )
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
