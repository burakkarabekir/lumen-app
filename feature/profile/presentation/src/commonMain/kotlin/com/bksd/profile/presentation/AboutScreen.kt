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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.component.layout.AppBarStyle
import com.bksd.core.design_system.component.layout.AppScaffold
import com.bksd.core.design_system.component.layout.AppSurface
import com.bksd.core.design_system.component.layout.AppTopBar
import com.bksd.core.design_system.theme.PreviewAppTheme
import com.bksd.core.domain.appinfo.AppInfoProvider
import com.bksd.profile.presentation.components.AboutFooter
import com.bksd.profile.presentation.components.AboutHeader
import com.bksd.profile.presentation.components.ProfileSettingsRow
import com.bksd.profile.presentation.components.SectionHeader
import com.bksd.profile.presentation.components.SettingsGroup
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

private val AccentLegal = Color(0xFF8A6FBF)
private val AccentWeb = Color(0xFF6E7AD0)
private val AccentRate = Color(0xFFE0A33A)

private const val PrivacyPolicyUrl = "https://lumenjournal.app/privacy"
private const val TermsUrl = "https://lumenjournal.app/terms"
private const val WebsiteUrl = "https://lumenjournal.app"
private const val LicensesUrl = "https://lumenjournal.app/licenses"

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
                                contentDescription = "Back"
                            )
                        }
                    },
                )
            }
        ) {
            AboutHeader(version = version)

            SectionHeader(stringResource(Res.string.about_section_legal))
            Spacer(Modifier.height(8.dp))
            SettingsGroup {
                ProfileSettingsRow(
                    icon = Icons.Default.Lock,
                    label = stringResource(Res.string.about_privacy_policy),
                    accent = AccentLegal,
                    onClick = { runCatching { uriHandler.openUri(PrivacyPolicyUrl) } }
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                    modifier = Modifier.padding(start = 61.dp)
                )
                ProfileSettingsRow(
                    icon = Icons.Default.Description,
                    label = stringResource(Res.string.about_terms),
                    accent = AccentLegal,
                    onClick = { runCatching { uriHandler.openUri(TermsUrl) } }
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                    modifier = Modifier.padding(start = 61.dp)
                )
                ProfileSettingsRow(
                    icon = Icons.Default.Language,
                    label = stringResource(Res.string.about_website),
                    accent = AccentWeb,
                    onClick = { runCatching { uriHandler.openUri(WebsiteUrl) } }
                )
            }

            Spacer(Modifier.height(20.dp))

            SectionHeader(stringResource(Res.string.about_section_more))
            Spacer(Modifier.height(8.dp))
            SettingsGroup {
                ProfileSettingsRow(
                    icon = Icons.Default.Star,
                    label = stringResource(Res.string.about_rate),
                    accent = AccentRate,
                    onClick = { runCatching { uriHandler.openUri(rateUrl) } }
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                    modifier = Modifier.padding(start = 61.dp)
                )
                ProfileSettingsRow(
                    icon = Icons.Default.Code,
                    label = stringResource(Res.string.about_acknowledgements),
                    accent = AccentWeb,
                    onClick = { runCatching { uriHandler.openUri(LicensesUrl) } }
                )
            }

            Spacer(Modifier.height(28.dp))
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
