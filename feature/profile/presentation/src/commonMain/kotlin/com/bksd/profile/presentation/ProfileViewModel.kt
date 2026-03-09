package com.bksd.profile.presentation

import com.bksd.core.presentation.util.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for the User Profile screen.
 * Does NOT own theme state — theme is controlled via [ThemeController] + [LocalThemeController].
 */
class ProfileViewModel : BaseViewModel<ProfileAction, ProfileEvent>() {

    private val _stateFlow = MutableStateFlow(createInitialState())
    val state: StateFlow<ProfileState> = _stateFlow.asStateFlow()

    override fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.OnSignOutClick -> handleSignOut()
            ProfileAction.OnUpgradeClick -> {
                sendEvent(ProfileEvent.NavigateToPaywall)
            }
            // Placeholders for future implementation
            ProfileAction.OnPrivacyClick,
            ProfileAction.OnDataExportClick,
            ProfileAction.OnThemeClick,
            ProfileAction.OnNotificationsClick,
            ProfileAction.OnEditAvatarClick,
            ProfileAction.OnSettingsClick,
            ProfileAction.OnBackClick -> {
                // No-op for Phase 2
            }
        }
    }

    private fun handleSignOut() {
        _stateFlow.update { it.copy(isSigningOut = true) }
        launch {
            // In Phase 3, this will call FirebaseAuthDataSource.signOut()
            _stateFlow.update { it.copy(isSigningOut = false) }
            sendEvent(ProfileEvent.SignOutSuccess)
        }
    }

    private fun createInitialState(): ProfileState = ProfileState(
        displayName = "Alex Morgan",
        role = "Product Manager",
        memberSince = "Member since 2023",
        avatarUrl = null,
        hasNotificationBadge = true,
        isSigningOut = false,
    )
}
