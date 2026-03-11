package com.bksd.profile.presentation

import androidx.lifecycle.viewModelScope
import com.bksd.core.domain.repository.GetProfileAvatarUseCase
import com.bksd.core.domain.repository.SetProfileAvatarUseCase
import com.bksd.core.presentation.util.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ProfileViewModel(
    private val getProfileAvatarUseCase: GetProfileAvatarUseCase,
    private val setProfileAvatarUseCase: SetProfileAvatarUseCase
) : BaseViewModel<ProfileAction, ProfileEvent>() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ProfileState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeAvatar()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ProfileState()
        )

    private fun observeAvatar() {
        launch {
            getProfileAvatarUseCase().collect { url ->
                _state.update { it.copy(avatarUrl = url, isAvatarLoading = false) }
            }
        }
    }

    override fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.OnSignOutClick -> handleSignOut()
            ProfileAction.OnUpgradeClick -> sendEvent(ProfileEvent.NavigateToPaywall)
            ProfileAction.OnUploadPictureClick -> sendEvent(ProfileEvent.OpenPhotoPicker)

            is ProfileAction.OnPictureSelected -> {
                _state.update { it.copy(isAvatarLoading = true) }
                launch {
                    try {
                        setProfileAvatarUseCase(action.bytes, action.mimeType)
                    } catch (e: Exception) {
                        sendEvent(ProfileEvent.PermissionError(e.message ?: "Unknown error"))
                    } finally {
                        _state.update { it.copy(isAvatarLoading = false) }
                    }
                }
            }

            ProfileAction.OnPrivacyClick,
            ProfileAction.OnDataExportClick,
            ProfileAction.OnThemeClick,
            ProfileAction.OnNotificationsClick,
            ProfileAction.OnSettingsClick -> Unit
        }
    }

    private fun handleSignOut() {
        _state.update { it.copy(isSigningOut = true) }
        launch {
            _state.update { it.copy(isSigningOut = false) }
            sendEvent(ProfileEvent.SignOutSuccess)
        }
    }
}
