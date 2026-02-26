package com.bksd.profile.presentation

import com.bksd.core.presentation.util.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for the User Profile screen.
 * Populates state with fake data for Phase 2 (UI-first development).
 * Sign-out triggers a navigation event.
 */
class ProfileViewModel : BaseViewModel<ProfileAction, ProfileEvent>() {

    private val _stateFlow = MutableStateFlow(createFakeState())
    val state: StateFlow<ProfileState> = _stateFlow.asStateFlow()

    override fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.OnSignOutClick -> handleSignOut()
            ProfileAction.OnUpgradeClick -> {
                sendEvent(ProfileEvent.NavigateToPaywall)
            }
            // These actions are placeholders for future implementation
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

    private fun createFakeState(): ProfileState = ProfileState(
        displayName = "Alex Morgan",
        role = "Product Manager",
        memberSince = "Member since 2023",
        avatarUrl = null,
        currentTheme = "Dark Mode",
        hasNotificationBadge = true,
        isSigningOut = false
    )
}
