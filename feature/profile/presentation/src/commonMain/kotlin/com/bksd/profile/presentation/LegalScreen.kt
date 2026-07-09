package com.bksd.profile.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bksd.core.design_system.component.layout.AppBarStyle
import com.bksd.core.design_system.component.layout.AppScaffold
import com.bksd.core.design_system.component.layout.AppSurface
import com.bksd.core.design_system.component.layout.AppTopBar
import com.bksd.core.design_system.theme.PreviewAppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.profileAccentViolet
import com.bksd.core.domain.legal.LegalConfig
import com.bksd.profile.presentation.components.ProfileSettingsRow
import com.bksd.profile.presentation.components.SettingsGroup
import org.jetbrains.compose.resources.stringResource

@Composable
fun LegalRoot(
    onBack: () -> Unit,
    onOpenDocument: (url: String, title: String) -> Unit,
) {
    LegalScreen(
        onBack = onBack,
        onOpenDocument = onOpenDocument,
    )
}

@Composable
internal fun LegalScreen(
    onBack: () -> Unit,
    onOpenDocument: (url: String, title: String) -> Unit,
) {
    val termsTitle = stringResource(Res.string.legal_terms)
    val privacyTitle = stringResource(Res.string.legal_privacy)

    AppScaffold {
        AppSurface(
            enableScrolling = true,
            centered = true,
            header = {
                AppTopBar(
                    title = stringResource(Res.string.legal_title),
                    style = AppBarStyle.Child,
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(Res.string.action_back),
                            )
                        }
                    },
                )
            },
        ) {
            Text(
                text = stringResource(Res.string.legal_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = MaterialTheme.dimens.spacing.lg,
                        vertical = MaterialTheme.dimens.spacing.md,
                    ),
            )

            SettingsGroup {
                ProfileSettingsRow(
                    icon = Icons.Default.Description,
                    label = termsTitle,
                    accent = MaterialTheme.colorScheme.extended.profileAccentViolet,
                    onClick = { onOpenDocument(LegalConfig.TERMS_URL, termsTitle) },
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                    modifier = Modifier.padding(start = MaterialTheme.dimens.spacing.massive),
                )
                ProfileSettingsRow(
                    icon = Icons.Default.Lock,
                    label = privacyTitle,
                    accent = MaterialTheme.colorScheme.extended.profileAccentViolet,
                    onClick = { onOpenDocument(LegalConfig.PRIVACY_URL, privacyTitle) },
                )
            }

            Spacer(Modifier.height(MaterialTheme.dimens.spacing.xxxl))
        }
    }
}

@Preview
@Composable
private fun LegalScreenPreview() {
    PreviewAppTheme(darkTheme = true) {
        LegalScreen(
            onBack = {},
            onOpenDocument = { _, _ -> },
        )
    }
}
