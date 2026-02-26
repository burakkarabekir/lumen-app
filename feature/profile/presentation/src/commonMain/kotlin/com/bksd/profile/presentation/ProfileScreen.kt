package com.bksd.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bksd.core.design_system.component.layout.AppBarStyle
import com.bksd.core.design_system.component.layout.AppSurface
import com.bksd.core.design_system.component.layout.AppTopBar
import com.bksd.core.presentation.util.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileRoot(
    onNavigateToSignIn: () -> Unit,
    onNavigateToPaywall: () -> Unit
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
        onAction = viewModel::onAction
    )
}

@Composable
internal fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit
) {
    AppSurface(
        enableScrolling = true,
        centered = true,
        header = {
            // ==================== Top Bar ====================
            AppTopBar(
                title = "Profile",
                style = AppBarStyle.Root
            )
        }
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // ==================== Avatar ====================
        Box(contentAlignment = Alignment.BottomEnd) {
            // Avatar circle
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(40.dp)
                )
            }
            // Edit badge
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .offset(x = 2.dp, y = 2.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable { onAction(ProfileAction.OnEditAvatarClick) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit avatar",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ==================== Identity ====================
        Text(
            text = state.displayName,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = state.role,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "✓ ${state.memberSince}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ==================== Momentum Pro Card ====================
        MomentumProCard(
            onUpgradeClick = { onAction(ProfileAction.OnUpgradeClick) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ==================== ACCOUNT Section ====================
        SectionHeader("ACCOUNT")
        Spacer(modifier = Modifier.height(8.dp))

        ProfileSettingsRow(
            icon = Icons.Default.Lock,
            label = "Privacy & Security",
            onClick = { onAction(ProfileAction.OnPrivacyClick) }
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
        )
        ProfileSettingsRow(
            icon = Icons.Default.Download,
            label = "Data Export",
            onClick = { onAction(ProfileAction.OnDataExportClick) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ==================== PREFERENCES Section ====================
        SectionHeader("PREFERENCES")
        Spacer(modifier = Modifier.height(8.dp))

        ProfileSettingsRow(
            icon = Icons.Default.Palette,
            label = "App Theme",
            trailingValue = state.currentTheme,
            onClick = { onAction(ProfileAction.OnThemeClick) }
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
        )
        ProfileSettingsRow(
            icon = Icons.Default.Notifications,
            label = "Notifications",
            showBadge = state.hasNotificationBadge,
            onClick = { onAction(ProfileAction.OnNotificationsClick) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // ==================== Sign Out ====================
        if (state.isSigningOut) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.error,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = "Sign Out",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clickable { onAction(ProfileAction.OnSignOutClick) }
                    .padding(vertical = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(128.dp))
    } // End AppSurface
} // Missing closing brace for ProfileScreen

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp
        ),
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        modifier = modifier.fillMaxWidth()
    )
}
