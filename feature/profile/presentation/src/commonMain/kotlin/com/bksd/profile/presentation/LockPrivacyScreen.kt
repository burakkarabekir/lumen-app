package com.bksd.profile.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.component.layout.AppBarStyle
import com.bksd.core.design_system.component.layout.AppScaffold
import com.bksd.core.design_system.component.layout.AppSurface
import com.bksd.core.design_system.component.layout.AppTopBar
import com.bksd.core.design_system.theme.PreviewAppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.profileAccentGreen
import com.bksd.core.design_system.theme.profileAccentViolet
import com.bksd.core.domain.legal.LegalConfig
import com.bksd.core.presentation.biometric.BiometricResult
import com.bksd.core.presentation.biometric.rememberBiometricAuthenticator
import com.bksd.profile.presentation.components.ProfileSettingsRow
import com.bksd.profile.presentation.components.SectionHeader
import com.bksd.profile.presentation.components.SettingsGroup
import com.bksd.profile.presentation.components.SettingsInfoRow
import com.bksd.profile.presentation.components.SettingsToggleRow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LockPrivacyRoot(
    onBack: () -> Unit,
    onOpenDocument: (url: String, title: String) -> Unit,
) {
    val viewModel = koinViewModel<LockPrivacyViewModel>()
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val authenticator = rememberBiometricAuthenticator()
    val biometricAvailable = remember(authenticator) { authenticator.isAvailable() }

    val enableTitle = stringResource(Res.string.lock_prompt_enable_title)
    val enableSubtitle = stringResource(Res.string.lock_prompt_enable_subtitle)
    val errorMessage = stringResource(Res.string.lock_error)

    val onToggleAppLock: (Boolean) -> Unit = { enabled ->
        if (enabled) {
            scope.launch {
                val result = authenticator.authenticate(enableTitle, enableSubtitle)
                if (result == BiometricResult.SUCCESS) {
                    viewModel.onAction(LockPrivacyAction.OnSetAppLock(true))
                } else {
                    snackbarHostState.showSnackbar(errorMessage)
                }
            }
        } else {
            viewModel.onAction(LockPrivacyAction.OnSetAppLock(false))
        }
    }

    LockPrivacyScreen(
        state = state,
        biometricAvailable = biometricAvailable,
        snackbarHostState = snackbarHostState,
        onBack = onBack,
        onToggleAppLock = onToggleAppLock,
        onOpenDocument = onOpenDocument,
    )
}

@Composable
internal fun LockPrivacyScreen(
    state: LockPrivacyState,
    biometricAvailable: Boolean,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onToggleAppLock: (Boolean) -> Unit,
    onOpenDocument: (url: String, title: String) -> Unit,
) {
    val termsTitle = stringResource(Res.string.legal_terms)
    val privacyTitle = stringResource(Res.string.legal_privacy)

    AppScaffold(snackbarHostState = snackbarHostState) {
        AppSurface(
            enableScrolling = true,
            centered = true,
            header = {
                AppTopBar(
                    title = stringResource(Res.string.lock_privacy_title),
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
            SectionHeader(stringResource(Res.string.lock_section_security))
            Spacer(Modifier.height(MaterialTheme.dimens.spacing.sm))
            SettingsGroup {
                SettingsToggleRow(
                    icon = Icons.Default.Fingerprint,
                    label = stringResource(Res.string.lock_app_lock_title),
                    subtitle = stringResource(Res.string.lock_app_lock_subtitle),
                    checked = state.appLockEnabled,
                    onCheckedChange = onToggleAppLock,
                    enabled = biometricAvailable,
                    accent = MaterialTheme.colorScheme.extended.profileAccentGreen,
                )
            }
            if (!biometricAvailable) {
                Text(
                    text = stringResource(Res.string.lock_unavailable),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = MaterialTheme.dimens.spacing.lg,
                            vertical = MaterialTheme.dimens.spacing.sm,
                        ),
                )
            }

            Spacer(Modifier.height(MaterialTheme.dimens.spacing.xl))

            SectionHeader(stringResource(Res.string.lock_section_privacy))
            Spacer(Modifier.height(MaterialTheme.dimens.spacing.sm))
            SettingsGroup {
                ProfileSettingsRow(
                    icon = Icons.Default.Lock,
                    label = privacyTitle,
                    accent = MaterialTheme.colorScheme.extended.profileAccentViolet,
                    onClick = { onOpenDocument(LegalConfig.PRIVACY_URL, privacyTitle) },
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                    modifier = Modifier.padding(start = MaterialTheme.dimens.spacing.massive),
                )
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
                SettingsInfoRow(
                    icon = Icons.Default.Verified,
                    label = stringResource(Res.string.lock_policy_version),
                    value = state.policyVersion,
                    accent = MaterialTheme.colorScheme.extended.profileAccentViolet,
                )
            }

            Spacer(Modifier.height(128.dp))
        }
    }
}

@Preview
@Composable
private fun LockPrivacyScreenPreview() {
    PreviewAppTheme(darkTheme = true) {
        LockPrivacyScreen(
            state = LockPrivacyState(appLockEnabled = true, policyVersion = "2026-07-01"),
            biometricAvailable = true,
            snackbarHostState = SnackbarHostState(),
            onBack = {},
            onToggleAppLock = {},
            onOpenDocument = { _, _ -> },
        )
    }
}
