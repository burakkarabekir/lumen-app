package com.bksd.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.component.layout.AppBarStyle
import com.bksd.core.design_system.component.layout.AppScaffold
import com.bksd.core.design_system.component.layout.AppSurface
import com.bksd.core.design_system.component.layout.AppTopBar
import com.bksd.core.design_system.theme.PreviewAppTheme
import com.bksd.core.design_system.theme.dimens
import com.bksd.core.design_system.theme.extended
import com.bksd.core.design_system.theme.profileAccentAmber
import com.bksd.core.design_system.theme.profileAccentGreen
import com.bksd.core.design_system.theme.profileAccentIndigo
import com.bksd.core.design_system.theme.profileAccentViolet
import com.bksd.core.design_system.theme.rememberThemeController
import com.bksd.core.presentation.media.rememberImagePickerLauncher
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.profile.presentation.components.AppearanceRow
import com.bksd.profile.presentation.components.ProfileHeroCard
import com.bksd.profile.presentation.components.ProfilePhotoViewer
import com.bksd.profile.presentation.components.ProfileSettingsRow
import com.bksd.profile.presentation.components.RemindersSheet
import com.bksd.profile.presentation.components.SectionHeader
import com.bksd.profile.presentation.components.SettingsGroup
import com.bksd.profile.presentation.components.ThemeSelectorSheet
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileRoot(
    onBack: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    onNavigateToPaywall: () -> Unit,
    onNavigateToManagePremium: () -> Unit,
    onNavigateToCloudSync: () -> Unit,
    onNavigateToLockPrivacy: () -> Unit,
    onNavigateToExportJournal: () -> Unit,
    onNavigateToLegal: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToHelp: () -> Unit,
) {
    val viewModel = koinViewModel<ProfileViewModel>()
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val imagePickerLauncher = rememberImagePickerLauncher { pickedImageData ->
        viewModel.onAction(
            ProfileAction.OnPictureSelected(
                bytes = pickedImageData.bytes,
                mimeType = pickedImageData.mimeType
            )
        )
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ProfileEvent.SignOutSuccess -> onNavigateToSignIn()
            ProfileEvent.DeleteAccountSuccess -> onNavigateToSignIn()
            ProfileEvent.NavigateToPaywall -> onNavigateToPaywall()
            ProfileEvent.NavigateToManagePremium -> onNavigateToManagePremium()
            ProfileEvent.NavigateToCloudSync -> onNavigateToCloudSync()
            ProfileEvent.NavigateToLockPrivacy -> onNavigateToLockPrivacy()
            ProfileEvent.NavigateToExportJournal -> onNavigateToExportJournal()
            ProfileEvent.NavigateToLegal -> onNavigateToLegal()
            ProfileEvent.NavigateToEditProfile -> onNavigateToEditProfile()
            ProfileEvent.NavigateToAbout -> onNavigateToAbout()
            ProfileEvent.NavigateToHelp -> onNavigateToHelp()
            ProfileEvent.OpenPhotoPicker -> imagePickerLauncher.launch()

            is ProfileEvent.PermissionError -> {
                scope.launch {
                    val msg = getString(
                        Res.string.error_avatar_update_failed,
                        event.error.asStringAsync()
                    )
                    snackbarHostState.showSnackbar(msg)
                }
            }

            is ProfileEvent.SignOutError -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.error.asStringAsync())
                }
            }

            is ProfileEvent.DeleteAccountError -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.error.asStringAsync())
                }
            }
        }
    }

    ProfileScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onBack = onBack,
        onAction = viewModel::onAction,
    )
}

@Composable
internal fun ProfileScreen(
    state: ProfileState,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    onAction: (ProfileAction) -> Unit
) {
    val themeController = rememberThemeController()
    val themeMode by themeController.themeMode.collectAsState()
    var showRemindersSheet by remember { mutableStateOf(false) }
    var showThemeSheet by remember { mutableStateOf(false) }
    var showEnlargedPhoto by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        AppScaffold(
            snackbarHostState = snackbarHostState
        ) {
            AppSurface(
            enableScrolling = true,
            centered = true,
            header = {
                AppTopBar(
                    title = stringResource(Res.string.profile_title),
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
            ProfileHeroCard(
                name = state.name,
                subtitle = state.jobTitle,
                avatarUrl = state.photoUrl,
                isAvatarLoading = state.isAvatarLoading,
                entries = state.entriesCount,
                weeklyStreak = state.weeklyStreak,
                joinYear = state.joinYear,
                onAvatarClick = { if (!state.photoUrl.isNullOrBlank()) showEnlargedPhoto = true },
                onEditClick = { onAction(ProfileAction.OnEditProfileClick) },
                modifier = Modifier.padding(MaterialTheme.dimens.spacing.lg)
            )

            SectionHeader(stringResource(Res.string.section_subscription))
            Spacer(Modifier.height(MaterialTheme.dimens.spacing.sm))
            SettingsGroup {
                ProfileSettingsRow(
                    icon = Icons.Default.WorkspacePremium,
                    label = stringResource(Res.string.manage_premium),
                    accent = MaterialTheme.colorScheme.extended.profileAccentAmber,
                    trailingValue = stringResource(
                        if (state.isPremium) Res.string.plan_plus else Res.string.plan_free
                    ),
                    onClick = { onAction(ProfileAction.OnManagePremiumClick) }
                )
            }

            Spacer(Modifier.height(MaterialTheme.dimens.spacing.xl))

            SectionHeader(stringResource(Res.string.section_preferences))
            Spacer(Modifier.height(MaterialTheme.dimens.spacing.sm))
            SettingsGroup {
                AppearanceRow(
                    selectedMode = themeMode,
                    onClick = { showThemeSheet = true }
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                    modifier = Modifier.padding(start = MaterialTheme.dimens.spacing.massive)
                )
                ProfileSettingsRow(
                    icon = Icons.Default.Notifications,
                    label = stringResource(Res.string.reminders),
                    accent = MaterialTheme.colorScheme.extended.profileAccentIndigo,
                    onClick = { showRemindersSheet = true }
                )
            }

            Spacer(Modifier.height(MaterialTheme.dimens.spacing.xl))

            SectionHeader(stringResource(Res.string.section_data_privacy))
            Spacer(Modifier.height(MaterialTheme.dimens.spacing.sm))
            SettingsGroup {
                ProfileSettingsRow(
                    icon = Icons.Default.Cloud,
                    label = stringResource(Res.string.cloud_sync),
                    accent = MaterialTheme.colorScheme.extended.profileAccentGreen,
                    trailingValue = "On",
                    trailingColor = MaterialTheme.colorScheme.extended.profileAccentGreen,
                    onClick = { onAction(ProfileAction.OnCloudSyncClick) }
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                    modifier = Modifier.padding(start = MaterialTheme.dimens.spacing.massive)
                )
                ProfileSettingsRow(
                    icon = Icons.Default.Lock,
                    label = stringResource(Res.string.lock_privacy),
                    accent = MaterialTheme.colorScheme.extended.profileAccentGreen,
                    onClick = { onAction(ProfileAction.OnPrivacyClick) }
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                    modifier = Modifier.padding(start = MaterialTheme.dimens.spacing.massive)
                )
                ProfileSettingsRow(
                    icon = Icons.Default.Download,
                    label = stringResource(Res.string.export_journal),
                    accent = MaterialTheme.colorScheme.extended.profileAccentGreen,
                    onClick = { onAction(ProfileAction.OnDataExportClick) }
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                    modifier = Modifier.padding(start = MaterialTheme.dimens.spacing.massive)
                )
                ProfileSettingsRow(
                    icon = Icons.Default.DeleteForever,
                    label = stringResource(Res.string.delete_account),
                    accent = MaterialTheme.colorScheme.error,
                    onClick = { onAction(ProfileAction.OnDeleteAccountClick) }
                )
            }

            Spacer(Modifier.height(MaterialTheme.dimens.spacing.xl))

            SectionHeader(stringResource(Res.string.section_support))
            Spacer(Modifier.height(MaterialTheme.dimens.spacing.sm))
            SettingsGroup {
                ProfileSettingsRow(
                    icon = Icons.AutoMirrored.Filled.Help,
                    label = stringResource(Res.string.help_center),
                    accent = MaterialTheme.colorScheme.extended.profileAccentViolet,
                    onClick = { onAction(ProfileAction.OnHelpClick) }
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                    modifier = Modifier.padding(start = MaterialTheme.dimens.spacing.massive)
                )
                ProfileSettingsRow(
                    icon = Icons.Default.Info,
                    label = stringResource(Res.string.about_lumen),
                    accent = MaterialTheme.colorScheme.extended.profileAccentViolet,
                    onClick = { onAction(ProfileAction.OnAboutClick) }
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                    modifier = Modifier.padding(start = MaterialTheme.dimens.spacing.massive)
                )
                ProfileSettingsRow(
                    icon = Icons.Default.Gavel,
                    label = stringResource(Res.string.terms_privacy),
                    accent = MaterialTheme.colorScheme.extended.profileAccentViolet,
                    onClick = { onAction(ProfileAction.OnLegalClick) }
                )
            }

            Spacer(Modifier.height(MaterialTheme.dimens.spacing.xl))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.spacing.lg)
                    .clip(RoundedCornerShape(MaterialTheme.dimens.radius.lg))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable(enabled = !state.isSigningOut) { onAction(ProfileAction.OnSignOutClick) }
                    .padding(MaterialTheme.dimens.spacing.lg),
                contentAlignment = Alignment.Center
            ) {
                if (state.isSigningOut) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(MaterialTheme.dimens.icon.lg),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.spacing.sm),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(MaterialTheme.dimens.icon.md)
                        )
                        Text(
                            text = stringResource(Res.string.sign_out),
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
            Spacer(Modifier.height(128.dp))

                if (showRemindersSheet) {
                    RemindersSheet(onDismiss = { showRemindersSheet = false })
                }

                if (showThemeSheet) {
                    ThemeSelectorSheet(onDismiss = { showThemeSheet = false })
                }

                if (state.showDeleteDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            if (!state.isDeletingAccount) onAction(ProfileAction.OnDismissDeleteAccount)
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.DeleteForever,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        },
                        title = { Text(stringResource(Res.string.delete_account_title)) },
                        text = { Text(stringResource(Res.string.delete_account_message)) },
                        confirmButton = {
                            TextButton(
                                onClick = { onAction(ProfileAction.OnConfirmDeleteAccount) },
                                enabled = !state.isDeletingAccount
                            ) {
                                if (state.isDeletingAccount) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(MaterialTheme.dimens.icon.sm),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                } else {
                                    Text(
                                        text = stringResource(Res.string.delete_account_confirm),
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { onAction(ProfileAction.OnDismissDeleteAccount) },
                                enabled = !state.isDeletingAccount
                            ) {
                                Text(stringResource(Res.string.action_cancel))
                            }
                        }
                    )
                }
        }
        }

        ProfilePhotoViewer(
            visible = showEnlargedPhoto,
            photoUrl = state.photoUrl,
            onDismiss = { showEnlargedPhoto = false }
        )
    }
}

@Preview
@Composable
private fun ProfileScreenDarkPreview() {
    PreviewAppTheme(darkTheme = true) {
        ProfileScreen(
            state = ProfileState(
                name = "Burak Yılmaz",
                jobTitle = "Product Manager",
                joinYear = "2024",
                isPremium = true,
                entriesCount = 83,
                weeklyStreak = 15,
            ),
            snackbarHostState = SnackbarHostState(),
            onBack = {},
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun ProfileScreenLightPreview() {
    PreviewAppTheme(darkTheme = false) {
        ProfileScreen(
            state = ProfileState(
                name = "Burak Yılmaz",
                jobTitle = "Product Manager",
                joinYear = "2024",
                isPremium = false,
                entriesCount = 83,
                weeklyStreak = 15,
            ),
            snackbarHostState = SnackbarHostState(),
            onBack = {},
            onAction = {},
        )
    }
}
