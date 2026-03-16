package com.bksd.profile.presentation

import androidx.lifecycle.viewModelScope
import com.bksd.auth.domain.usecase.SignOutUseCase
import com.bksd.core.domain.error.Result
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.profile.domain.usecase.GetUserProfileUseCase
import com.bksd.profile.domain.usecase.SetProfileAvatarUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val setProfileAvatarUseCase: SetProfileAvatarUseCase,
    private val signOutUseCase: SignOutUseCase,
) : BaseViewModel<ProfileAction, ProfileEvent>() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ProfileState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadUserProfile()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ProfileState()
        )

    private fun loadUserProfile() {
        launch {
            val profile = getUserProfileUseCase()
            _state.update {
                it.copy(
                    name = profile.displayName,
                    photoUrl = profile.photoUrl,
                    jobTitle = profile.jobTitle,
                    joinYear = profile.joinYear,
                    isPremium = profile.isPremium,
                    isProfileLoading = false
                )
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
                        val downloadUrl = setProfileAvatarUseCase(action.bytes, action.mimeType)
                        _state.update { it.copy(photoUrl = downloadUrl, isAvatarLoading = false) }
                    } catch (e: Exception) {
                        sendEvent(ProfileEvent.PermissionError(e.message ?: "Unknown error"))
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
            val result = signOutUseCase()
            _state.update { it.copy(isSigningOut = false) }
            when (result) {
                is Result.Success -> sendEvent(ProfileEvent.SignOutSuccess)
                is Result.Error -> sendEvent(ProfileEvent.SignOutError(result.error.toString()))
            }
        }
    }
}
