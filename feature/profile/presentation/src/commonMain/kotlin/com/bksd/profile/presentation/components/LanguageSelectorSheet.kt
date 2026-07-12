package com.bksd.profile.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.bksd.core.design_system.theme.rememberLanguageController
import com.bksd.core.domain.language.AppLanguage
import com.bksd.profile.presentation.Res
import com.bksd.profile.presentation.language_selector_title
import com.bksd.profile.presentation.language_system
import org.jetbrains.compose.resources.stringResource

@Composable
fun LanguageSelectorSheet(
    onDismiss: () -> Unit,
) {
    val controller = rememberLanguageController()
    val current by controller.language.collectAsState()

    LanguageSelectorSheetContent(
        current = current,
        onSelected = { language ->
            controller.setLanguage(language)
            onDismiss()
        },
        onDismiss = onDismiss,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LanguageSelectorSheetContent(
    current: AppLanguage,
    onSelected: (AppLanguage) -> Unit,
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
                text = stringResource(Res.string.language_selector_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(
                    start = MaterialTheme.dimens.spacing.md,
                    top = MaterialTheme.dimens.spacing.sm,
                    bottom = MaterialTheme.dimens.spacing.md,
                ),
            )

            LanguageOptionRow(
                label = stringResource(Res.string.language_system),
                isSelected = current == AppLanguage.SYSTEM,
                onClick = { onSelected(AppLanguage.SYSTEM) },
            )
            LanguageOptionRow(
                label = "English",
                isSelected = current == AppLanguage.ENGLISH,
                onClick = { onSelected(AppLanguage.ENGLISH) },
            )
            LanguageOptionRow(
                label = "Türkçe",
                isSelected = current == AppLanguage.TURKISH,
                onClick = { onSelected(AppLanguage.TURKISH) },
            )
            LanguageOptionRow(
                label = "Deutsch",
                isSelected = current == AppLanguage.GERMAN,
                onClick = { onSelected(AppLanguage.GERMAN) },
            )
            LanguageOptionRow(
                label = "Español",
                isSelected = current == AppLanguage.SPANISH,
                onClick = { onSelected(AppLanguage.SPANISH) },
            )
            LanguageOptionRow(
                label = "Français",
                isSelected = current == AppLanguage.FRENCH,
                onClick = { onSelected(AppLanguage.FRENCH) },
            )
        }
    }
}

@Preview
@Composable
private fun LanguageSelectorSheetContentPreview() {
    PreviewAppTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.xs),
        ) {
            LanguageOptionRow(label = "System default", isSelected = true, onClick = {})
            LanguageOptionRow(label = "English", isSelected = false, onClick = {})
            LanguageOptionRow(label = "Türkçe", isSelected = false, onClick = {})
        }
    }
}
