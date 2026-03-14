package com.bksd.lumen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bksd.core.domain.storage.SessionStorage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionStorage: SessionStorage
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(MainState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeSession()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = MainState()
        )

    private val eventChannel = Channel<MainEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val isLoggedIn = sessionStorage.isLoggedIn()
            _state.update {
                it.copy(
                    isCheckingAuth = false,
                    isLoggedIn = isLoggedIn
                )
            }
        }
    }

    private suspend fun observeSession() {
        sessionStorage.observeAuthState().collect { isAuthenticated ->
            val wasLoggedIn = _state.value.isLoggedIn
            _state.update { it.copy(isLoggedIn = isAuthenticated) }
            if (wasLoggedIn && !isAuthenticated) {
                eventChannel.send(MainEvent.OnSessionExpired)
            }
        }
    }
}