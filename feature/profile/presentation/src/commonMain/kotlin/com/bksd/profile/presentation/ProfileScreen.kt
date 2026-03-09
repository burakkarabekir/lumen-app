package com.bksd.profile.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bksd.core.design_system.component.button.AppButton
import com.bksd.core.design_system.component.button.AppButtonStyle
import com.bksd.core.design_system.component.layout.AppBarStyle
import com.bksd.core.design_system.component.layout.AppSurface
import com.bksd.core.design_system.component.layout.AppTopBar
import com.bksd.core.design_system.theme.PreviewAppTheme
import com.bksd.core.design_system.theme.rememberThemeController
import com.bksd.core.domain.theme.AppThemeMode
import com.bksd.core.presentation.util.ObserveAsEvents
import com.bksd.profile.presentation.components.MomentumProCard
import com.bksd.profile.presentation.components.ProfileHeader
import com.bksd.profile.presentation.components.ProfileSettingsRow
import com.bksd.profile.presentation.components.SectionHeader
import com.bksd.profile.presentation.components.SettingsGroup
import com.bksd.profile.presentation.components.ThemeSelectorSheet
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileRoot(
    onNavigateToSignIn: () -> Unit,
    onNavigateToPaywall: () -> Unit,
) {
    val viewModel = koinViewModel<ProfileViewModel>()
    val state by viewModel.state.collectAsState()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ProfileEvent.SignOutSuccess -> onNavigateToSignIn()
            ProfileEvent.NavigateToPaywall -> onNavigateToPaywall()
        }
    }

    ProfileScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
internal fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit,
) {
    // Theme accessed via CompositionLocal — not owned by ProfileViewModel
    val themeController = rememberThemeController()
    val currentThemeMode by themeController.themeMode.collectAsState()

    // Local UI state for bottom sheet visibility
    var showThemeSheet by rememberSaveable { mutableStateOf(false) }

    // Resolve theme mode to display label
    val themeLabel = when (currentThemeMode) {
        AppThemeMode.SYSTEM -> stringResource(Res.string.theme_system)
        AppThemeMode.LIGHT -> stringResource(Res.string.theme_light)
        AppThemeMode.DARK -> stringResource(Res.string.theme_dark)
    }

    // Stable lambda references to avoid recomposition-triggered allocations
    val onEditAvatar = remember(onAction) { { onAction(ProfileAction.OnEditAvatarClick) } }
    val onUpgrade = remember(onAction) { { onAction(ProfileAction.OnUpgradeClick) } }
    val onPrivacy = remember(onAction) { { onAction(ProfileAction.OnPrivacyClick) } }
    val onDataExport = remember(onAction) { { onAction(ProfileAction.OnDataExportClick) } }
    val onNotifications = remember(onAction) { { onAction(ProfileAction.OnNotificationsClick) } }
    val onSignOut = remember(onAction) { { onAction(ProfileAction.OnSignOutClick) } }

    // ==================== Theme Selector Sheet ====================
    if (showThemeSheet) {
        ThemeSelectorSheet(
            onDismiss = { showThemeSheet = false },
        )
    }

    AppSurface(
        enableScrolling = true,
        centered = true,
        header = {
            AppTopBar(
                title = stringResource(Res.string.profile_title),
                style = AppBarStyle.Root,
            )
        }
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // ==================== Header (Avatar + Identity) ====================
        ProfileHeader(
            displayName = state.displayName,
            role = state.role,
            memberSince = state.memberSince,
            onEditAvatarClick = onEditAvatar,
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ==================== Momentum Pro Card ====================
        MomentumProCard(
            onUpgradeClick = onUpgrade,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ==================== ACCOUNT Section ====================
        SectionHeader(stringResource(Res.string.section_account))
        Spacer(modifier = Modifier.height(8.dp))

        SettingsGroup {
            ProfileSettingsRow(
                icon = Icons.Default.Lock,
                label = stringResource(Res.string.privacy_security),
                onClick = onPrivacy,
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            ProfileSettingsRow(
                icon = Icons.Default.Download,
                label = stringResource(Res.string.data_export),
                onClick = onDataExport,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ==================== PREFERENCES Section ====================
        SectionHeader(stringResource(Res.string.section_preferences))
        Spacer(modifier = Modifier.height(8.dp))

        SettingsGroup {
            ProfileSettingsRow(
                icon = Icons.Default.Palette,
                label = stringResource(Res.string.app_theme),
                trailingValue = themeLabel,
                onClick = { showThemeSheet = true },
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            ProfileSettingsRow(
                icon = Icons.Default.Notifications,
                label = stringResource(Res.string.notifications),
                showBadge = state.hasNotificationBadge,
                onClick = onNotifications,
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ==================== Sign Out ====================
        AppButton(
            text = stringResource(Res.string.sign_out),
            onClick = onSignOut,
            style = AppButtonStyle.TEXT,
            isLoading = state.isSigningOut,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(128.dp))
    }
}

// ==================== Previews ====================

@Preview
@Composable
private fun ProfileScreenDarkPreview() {
    PreviewAppTheme(darkTheme = true) {
        ProfileScreen(
            state = ProfileState(
                displayName = "Alex Morgan",
                role = "Product Manager",
                memberSince = "Member since 2023",
                hasNotificationBadge = true,
            ),
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
                displayName = "Alex Morgan",
                role = "Product Manager",
                memberSince = "Member since 2023",
                hasNotificationBadge = true,
            ),
            onAction = {},
        )
    }
}
