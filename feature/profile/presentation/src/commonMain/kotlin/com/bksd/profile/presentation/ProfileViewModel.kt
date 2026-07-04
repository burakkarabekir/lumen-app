package com.bksd.profile.presentation

import androidx.lifecycle.viewModelScope
import com.bksd.auth.domain.usecase.DeleteAccountUseCase
import com.bksd.auth.domain.usecase.SignOutUseCase
import com.bksd.core.domain.error.Result
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.core.presentation.util.toUiText
import com.bksd.insights.domain.calculator.InsightsCalculator
import com.bksd.insights.domain.model.InsightsRange
import com.bksd.insights.domain.usecase.ObserveAllMomentsUseCase
import com.bksd.profile.domain.usecase.ObserveUserProfileUseCase
import com.bksd.profile.domain.usecase.SetProfileAvatarUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ProfileViewModel(
    private val observeUserProfileUseCase: ObserveUserProfileUseCase,
    private val setProfileAvatarUseCase: SetProfileAvatarUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val observeAllMoments: ObserveAllMomentsUseCase,
    private val insightsCalculator: InsightsCalculator,
) : BaseViewModel<ProfileAction, ProfileEvent>() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ProfileState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeProfile()
                observeStats()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ProfileState()
        )

    private fun observeProfile() {
        launch {
            observeUserProfileUseCase().collect { result ->
                when (result) {
                    is Result.Success -> {
                        val profile = result.data
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

                    is Result.Error -> {
                        _state.update { it.copy(isProfileLoading = false) }
                    }
                }
            }
        }
    }

    private fun observeStats() {
        launch {
            observeAllMoments().collect { moments ->
                val weeklyStreak = insightsCalculator
                    .compute(moments, InsightsRange.AllTime)
                    .currentWeekly?.length ?: 0
                _state.update {
                    it.copy(entriesCount = moments.size, weeklyStreak = weeklyStreak)
                }
            }
        }
    }

    override fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.OnSignOutClick -> handleSignOut()
            ProfileAction.OnUpgradeClick -> sendEvent(ProfileEvent.NavigateToPaywall)
            ProfileAction.OnUploadPictureClick -> sendEvent(ProfileEvent.OpenPhotoPicker)
            ProfileAction.OnEditProfileClick -> sendEvent(ProfileEvent.NavigateToEditProfile)
            ProfileAction.OnAboutClick -> sendEvent(ProfileEvent.NavigateToAbout)
            ProfileAction.OnHelpClick -> sendEvent(ProfileEvent.NavigateToHelp)
            ProfileAction.OnDeleteAccountClick -> _state.update { it.copy(showDeleteDialog = true) }
            ProfileAction.OnDismissDeleteAccount -> _state.update { it.copy(showDeleteDialog = false) }
            ProfileAction.OnConfirmDeleteAccount -> handleDeleteAccount()

            is ProfileAction.OnPictureSelected -> {
                _state.update { it.copy(isAvatarLoading = true) }
                launch {
                    when (val result = setProfileAvatarUseCase(action.bytes, action.mimeType)) {
                        is Result.Success -> {
                            _state.update { it.copy(photoUrl = result.data, isAvatarLoading = false) }
                        }
                        is Result.Error -> {
                            sendEvent(ProfileEvent.PermissionError(result.error.toUiText()))
                            _state.update { it.copy(isAvatarLoading = false) }
                        }
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
                is Result.Error -> sendEvent(ProfileEvent.SignOutError(result.error.toUiText()))
            }
        }
    }

    private fun handleDeleteAccount() {
        _state.update { it.copy(isDeletingAccount = true) }
        launch {
            val result = deleteAccountUseCase()
            _state.update { it.copy(isDeletingAccount = false, showDeleteDialog = false) }
            when (result) {
                is Result.Success -> sendEvent(ProfileEvent.DeleteAccountSuccess)
                is Result.Error -> sendEvent(ProfileEvent.DeleteAccountError(result.error.toUiText()))
            }
        }
    }
}
