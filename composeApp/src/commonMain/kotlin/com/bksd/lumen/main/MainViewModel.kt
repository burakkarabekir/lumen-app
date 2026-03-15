package com.bksd.lumen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bksd.core.domain.storage.SessionStorage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionStorage: SessionStorage
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    private val eventChannel = Channel<MainEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        _state.update {
            it.copy(
                isReady = true,
                isLoggedIn = sessionStorage.isLoggedIn()
            )
        }
        viewModelScope.launch {
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