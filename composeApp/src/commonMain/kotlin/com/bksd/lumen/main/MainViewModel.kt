package com.bksd.lumen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bksd.core.data.remote.firebase.FirebaseAuthDataSource
import com.bksd.core.domain.storage.SessionStorage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionStorage: SessionStorage,
    private val firebaseAuthDataSource: FirebaseAuthDataSource,
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    private val eventChannel = Channel<MainEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            var isLoggedIn = sessionStorage.isLoggedIn()

            if (isLoggedIn) {
                val rememberMe = sessionStorage.isRememberMeEnabled().first()
                if (!rememberMe) {
                    firebaseAuthDataSource.signOut()
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
                    eventChannel.send(MainEvent.OnSessionExpired)
                }
            }
        }
    }
}