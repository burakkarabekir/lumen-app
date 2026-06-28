package com.bksd.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.component.layout.AppBarStyle
import com.bksd.core.design_system.component.layout.AppScaffold
import com.bksd.core.design_system.component.layout.AppSurface
import com.bksd.core.design_system.component.layout.AppTopBar
import com.bksd.core.design_system.theme.PreviewAppTheme
import com.bksd.core.design_system.theme.rememberThemeController
import com.bksd.core.domain.theme.AppThemeMode
import com.bksd.core.presentation.media.rememberImagePickerLauncher
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.profile.presentation.components.AppearanceRow
import com.bksd.profile.presentation.components.ProfileHeroCard
import com.bksd.profile.presentation.components.ProfileSettingsRow
import com.bksd.profile.presentation.components.SectionHeader
import com.bksd.profile.presentation.components.SettingsGroup
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private val AccentPreferences = Color(0xFF6E7AD0)
private val AccentData = Color(0xFF2FA876)
private val AccentSupport = Color(0xFF8A6FBF)

@Composable
fun ProfileRoot(
    onBack: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    onNavigateToPaywall: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
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
            ProfileEvent.NavigateToPaywall -> onNavigateToPaywall()
            ProfileEvent.NavigateToEditProfile -> onNavigateToEditProfile()
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
    val isDark = themeMode == AppThemeMode.DARK ||
            (themeMode == AppThemeMode.SYSTEM && isSystemInDarkTheme())

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
                                contentDescription = "Back"
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
                onAvatarClick = { onAction(ProfileAction.OnUploadPictureClick) },
                onEditClick = { onAction(ProfileAction.OnEditProfileClick) },
                modifier = Modifier.padding(16.dp)
            )

            SectionHeader(stringResource(Res.string.section_preferences))
            Spacer(Modifier.height(8.dp))
            SettingsGroup {
                AppearanceRow(
                    isDark = isDark,
                    onSelectLight = { themeController.setTheme(AppThemeMode.LIGHT) },
                    onSelectDark = { themeController.setTheme(AppThemeMode.DARK) }
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                    modifier = Modifier.padding(start = 61.dp)
                )
                ProfileSettingsRow(
                    icon = Icons.Default.Notifications,
                    label = stringResource(Res.string.reminders),
                    accent = AccentPreferences,
                    trailingValue = "9:00 PM",
                    onClick = {}
                )
            }

            Spacer(Modifier.height(20.dp))

            SectionHeader(stringResource(Res.string.section_data_privacy))
            Spacer(Modifier.height(8.dp))
            SettingsGroup {
                ProfileSettingsRow(
                    icon = Icons.Default.Cloud,
                    label = stringResource(Res.string.cloud_sync),
                    accent = AccentData,
                    trailingValue = "On",
                    trailingColor = AccentData,
                    onClick = {}
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                    modifier = Modifier.padding(start = 61.dp)
                )
                ProfileSettingsRow(
                    icon = Icons.Default.Lock,
                    label = stringResource(Res.string.lock_privacy),
                    accent = AccentData,
                    onClick = { onAction(ProfileAction.OnPrivacyClick) }
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                    modifier = Modifier.padding(start = 61.dp)
                )
                ProfileSettingsRow(
                    icon = Icons.Default.Download,
                    label = stringResource(Res.string.export_journal),
                    accent = AccentData,
                    onClick = { onAction(ProfileAction.OnDataExportClick) }
                )
            }

            Spacer(Modifier.height(20.dp))

            SectionHeader(stringResource(Res.string.section_support))
            Spacer(Modifier.height(8.dp))
            SettingsGroup {
                ProfileSettingsRow(
                    icon = Icons.AutoMirrored.Filled.Help,
                    label = stringResource(Res.string.help_center),
                    accent = AccentSupport,
                    onClick = {}
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                    modifier = Modifier.padding(start = 61.dp)
                )
                ProfileSettingsRow(
                    icon = Icons.Default.Info,
                    label = stringResource(Res.string.about_lumen),
                    accent = AccentSupport,
                    trailingValue = "v2.4.1",
                    onClick = {}
                )
            }

            Spacer(Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable(enabled = !state.isSigningOut) { onAction(ProfileAction.OnSignOutClick) }
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                if (state.isSigningOut) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
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
        }
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
