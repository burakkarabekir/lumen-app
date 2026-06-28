package com.bksd.profile.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.component.layout.AppBarStyle
import com.bksd.core.design_system.component.layout.AppScaffold
import com.bksd.core.design_system.component.layout.AppSurface
import com.bksd.core.design_system.component.layout.AppTopBar
import com.bksd.core.design_system.theme.PreviewAppTheme
import com.bksd.core.design_system.theme.rememberNewEntryPalette
import com.bksd.profile.presentation.components.HelpHeader
import com.bksd.profile.presentation.components.ProfileSettingsRow
import com.bksd.profile.presentation.components.SectionHeader
import com.bksd.profile.presentation.components.SettingsGroup
import org.jetbrains.compose.resources.stringResource

private val AccentContact = Color(0xFF6E7AD0)
private val AccentBug = Color(0xFFD0584F)

private const val SupportMailto = "mailto:support@lumenjournal.app"
private const val BugReportMailto = "mailto:support@lumenjournal.app?subject=Bug%20Report"

@Composable
fun HelpRoot(onBack: () -> Unit) {
    HelpScreen(onBack = onBack)
}

@Composable
internal fun HelpScreen(onBack: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    val palette = rememberNewEntryPalette()

    AppScaffold {
        AppSurface(
            enableScrolling = true,
            centered = true,
            header = {
                AppTopBar(
                    title = stringResource(Res.string.help_title),
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
            HelpHeader()
            Spacer(Modifier.height(20.dp))

            SectionHeader(stringResource(Res.string.help_section_contact))
            Spacer(Modifier.height(8.dp))
            SettingsGroup {
                ProfileSettingsRow(
                    icon = Icons.Default.MailOutline,
                    label = stringResource(Res.string.help_email_support),
                    accent = AccentContact,
                    onClick = { runCatching { uriHandler.openUri(SupportMailto) } }
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                    modifier = Modifier.padding(start = 61.dp)
                )
                ProfileSettingsRow(
                    icon = Icons.Default.BugReport,
                    label = stringResource(Res.string.help_report_bug),
                    accent = AccentBug,
                    onClick = { runCatching { uriHandler.openUri(BugReportMailto) } }
                )
            }

            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(Res.string.help_response_note),
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Medium,
                color = palette.sub,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )
            Spacer(Modifier.height(128.dp))
        }
    }
}

@Preview
@Composable
private fun HelpScreenPreview() {
    PreviewAppTheme(darkTheme = true) {
        HelpScreen(onBack = {})
    }
}
