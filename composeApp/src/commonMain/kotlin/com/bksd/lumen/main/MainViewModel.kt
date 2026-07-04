package com.bksd.lumen.main

import com.bksd.auth.domain.AuthRepository
import com.bksd.core.domain.billing.EntitlementRepository
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
    private val entitlementRepository: EntitlementRepository,
) : BaseViewModel<Nothing, MainEvent>() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    init {
        launch {
            sessionStorage.awaitReady()
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
            syncRevenueCatUser(isLoggedIn)

            sessionStorage.observeAuthState().collect { isAuthenticated ->
                val wasLoggedIn = _state.value.isLoggedIn
                _state.update { it.copy(isLoggedIn = isAuthenticated) }
                syncRevenueCatUser(isAuthenticated)
                if (wasLoggedIn && !isAuthenticated) {
                    sendEvent(MainEvent.OnSessionExpired)
                }
            }
        }
    }

    private suspend fun syncRevenueCatUser(loggedIn: Boolean) {
        val uid = authRepository.getSignedInUserId()
        if (loggedIn && uid != null) {
            entitlementRepository.setUser(uid)
        } else {
            entitlementRepository.clearUser()
        }
    }
}
