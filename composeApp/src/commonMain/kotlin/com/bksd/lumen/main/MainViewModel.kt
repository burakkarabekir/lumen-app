package com.bksd.lumen.main

import com.bksd.auth.domain.AuthRepository
import com.bksd.core.domain.storage.SessionStorage
import com.bksd.core.presentation.util.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update

class MainViewModel(
    private val sessionStorage: SessionStorage,
    private val authRepository: AuthRepository,
) : BaseViewModel<Nothing, MainEvent>() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    init {
        launch {
            var isLoggedIn = sessionStorage.isLoggedIn()

            if (isLoggedIn) {
                val rememberMe = sessionStorage.isRememberMeEnabled().first()
                if (!rememberMe) {
                    authRepository.signOut()
                    isLoggedIn = false
                }
            }

            _state.update {
                it.copy(isReady = true, isLoggedIn = isLoggedIn)
            }

            sessionStorage.observeAuthState().collect { isAuthenticated ->
                val wasLoggedIn = _state.value.isLoggedIn
                _state.update { it.copy(isLoggedIn = isAuthenticated) }
                if (wasLoggedIn && !isAuthenticated) {
                    sendEvent(MainEvent.OnSessionExpired)
                }
            }
        }
    }
}
