package com.bksd.lumen.lock

import androidx.lifecycle.viewModelScope
import com.bksd.auth.domain.AuthRepository
import com.bksd.core.domain.applock.AppLockRepository
import com.bksd.core.presentation.util.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class LockGateViewModel(
    authRepository: AuthRepository,
    appLockRepository: AppLockRepository,
) : BaseViewModel<Nothing, Nothing>() {

    private val _state = MutableStateFlow(LockGateState())
    val state: StateFlow<LockGateState> = _state.asStateFlow()

    init {
        combine(
            authRepository.authState,
            appLockRepository.observeAppLockEnabled(),
        ) { loggedIn, lockEnabled -> loggedIn && lockEnabled }
            .onEach { effective ->
                _state.update { it.copy(enabled = effective, loaded = true) }
            }
            .launchIn(viewModelScope)
    }
}
