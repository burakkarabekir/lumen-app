package com.bksd.lumen.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bksd.auth.domain.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    private val eventChannel = Channel<MainEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val isLoggedIn = authRepository.getSignedInUserId() != null
            _state.update {
                it.copy(
                    isCheckingAuth = false,
                    isLoggedIn = isLoggedIn
                )
            }
            observeSession()
        }
    }

    private suspend fun observeSession() {
        authRepository.authState.collect { isAuthenticated ->
            val wasLoggedIn = _state.value.isLoggedIn
            _state.update { it.copy(isLoggedIn = isAuthenticated) }
            if (wasLoggedIn && !isAuthenticated) {
                eventChannel.send(MainEvent.OnSessionExpired)
            }
        }
    }
}

data class MainState(
    val isCheckingAuth: Boolean = true,
    val isLoggedIn: Boolean = false
)

sealed interface MainEvent {
    data object OnSessionExpired : MainEvent
}
