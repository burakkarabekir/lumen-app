package com.bksd.profile.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.component.layout.AppBarStyle
import com.bksd.core.design_system.component.layout.AppScaffold
import com.bksd.core.design_system.component.layout.AppSurface
import com.bksd.core.design_system.component.layout.AppTopBar
import com.bksd.core.design_system.theme.PreviewAppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.profileAccentAmber
import com.bksd.core.design_system.theme.profileAccentIndigo
import com.bksd.core.design_system.theme.profileAccentViolet
import com.bksd.core.domain.appinfo.AppInfoProvider
import com.bksd.core.domain.legal.LegalConfig
import com.bksd.profile.presentation.components.AboutFooter
import com.bksd.profile.presentation.components.AboutHeader
import com.bksd.profile.presentation.components.ProfileSettingsRow
import com.bksd.profile.presentation.components.SectionHeader
import com.bksd.profile.presentation.components.SettingsGroup
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

private const val WebsiteUrl = "https://lumenjournalapp.com"
private const val LicensesUrl = "https://lumenjournalapp.com/licenses"

@Composable
fun AboutRoot(onBack: () -> Unit) {
    val appInfo = koinInject<AppInfoProvider>()
    val version = remember(appInfo) {
        val name = appInfo.versionName.ifBlank { "1.0" }
        val build = appInfo.buildNumber
        if (build.isBlank()) name else "$name ($build)"
    }
    AboutScreen(version = version, rateUrl = appInfo.storeUrl, onBack = onBack)
}

@Composable
internal fun AboutScreen(
    version: String,
    rateUrl: String,
    onBack: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    AppScaffold {
        AppSurface(
            enableScrolling = true,
            centered = true,
            header = {
                AppTopBar(
                    title = stringResource(Res.string.about_title),
                    style = AppBarStyle.Child,
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(Res.string.action_back)
                            )
                        }
                    },
                )
            }
        ) {
            AboutHeader(version = version)

            SectionHeader(stringResource(Res.string.about_section_legal))
            Spacer(Modifier.height(MaterialTheme.dimens.spacing.sm))
            SettingsGroup {
                ProfileSettingsRow(
                    icon = Icons.Default.Lock,
                    label = stringResource(Res.string.about_privacy_policy),
                    accent = MaterialTheme.colorScheme.extended.profileAccentViolet,
                    onClick = { runCatching { uriHandler.openUri(LegalConfig.PRIVACY_URL) } }
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                    modifier = Modifier.padding(start = MaterialTheme.dimens.spacing.massive)
                )
                ProfileSettingsRow(
                    icon = Icons.Default.Description,
                    label = stringResource(Res.string.about_terms),
                    accent = MaterialTheme.colorScheme.extended.profileAccentViolet,
                    onClick = { runCatching { uriHandler.openUri(LegalConfig.TERMS_URL) } }
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                    modifier = Modifier.padding(start = MaterialTheme.dimens.spacing.massive)
                )
                ProfileSettingsRow(
                    icon = Icons.Default.Language,
                    label = stringResource(Res.string.about_website),
                    accent = MaterialTheme.colorScheme.extended.profileAccentIndigo,
                    onClick = { runCatching { uriHandler.openUri(WebsiteUrl) } }
                )
            }

            Spacer(Modifier.height(MaterialTheme.dimens.spacing.xl))

            SectionHeader(stringResource(Res.string.about_section_more))
            Spacer(Modifier.height(MaterialTheme.dimens.spacing.sm))
            SettingsGroup {
                ProfileSettingsRow(
                    icon = Icons.Default.Star,
                    label = stringResource(Res.string.about_rate),
                    accent = MaterialTheme.colorScheme.extended.profileAccentAmber,
                    onClick = { runCatching { uriHandler.openUri(rateUrl) } }
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                    modifier = Modifier.padding(start = MaterialTheme.dimens.spacing.massive)
                )
                ProfileSettingsRow(
                    icon = Icons.Default.Code,
                    label = stringResource(Res.string.about_acknowledgements),
                    accent = MaterialTheme.colorScheme.extended.profileAccentIndigo,
                    onClick = { runCatching { uriHandler.openUri(LicensesUrl) } }
                )
            }

            Spacer(Modifier.height(MaterialTheme.dimens.spacing.xxxl))
            AboutFooter()
            Spacer(Modifier.height(128.dp))
        }
    }
}

@Preview
@Composable
private fun AboutScreenPreview() {
    PreviewAppTheme(darkTheme = true) {
        AboutScreen(
            version = "1.0 (1)",
            rateUrl = "https://play.google.com/store/apps/details?id=com.bksd.lumen",
            onBack = {}
        )
    }
}
