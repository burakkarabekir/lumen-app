package com.bksd.profile.presentation

import androidx.lifecycle.viewModelScope
import com.bksd.core.domain.applock.AppLockRepository
import com.bksd.core.domain.legal.LegalConfig
import com.bksd.core.presentation.util.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class LockPrivacyViewModel(
    private val appLockRepository: AppLockRepository,
) : BaseViewModel<LockPrivacyAction, LockPrivacyEvent>() {

    private var hasLoaded = false

    private val initialState = LockPrivacyState(policyVersion = LegalConfig.POLICY_VERSION_DISPLAY)

    private val _state = MutableStateFlow(initialState)
    val state = _state
        .onStart {
            if (!hasLoaded) {
                hasLoaded = true
                observeLock()
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = initialState,
        )

    private fun observeLock() {
        launch {
            appLockRepository.observeAppLockEnabled().collect { enabled ->
                _state.update { it.copy(appLockEnabled = enabled) }
            }
        }
    }

    override fun onAction(action: LockPrivacyAction) {
        when (action) {
            is LockPrivacyAction.OnSetAppLock -> launch {
                appLockRepository.setAppLockEnabled(action.enabled)
            }
        }
    }
}
