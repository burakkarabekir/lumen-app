package com.bksd.lumen.consent

import androidx.lifecycle.viewModelScope
import com.bksd.auth.domain.AuthRepository
import com.bksd.core.domain.consent.ConsentRepository
import com.bksd.core.domain.error.Result
import com.bksd.core.presentation.util.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class ConsentGateViewModel(
    private val consentRepository: ConsentRepository,
    authRepository: AuthRepository,
) : BaseViewModel<Nothing, Nothing>() {

    private val _state = MutableStateFlow(ConsentGateState())
    val state: StateFlow<ConsentGateState> = _state.asStateFlow()

    init {
        authRepository.authState
            .onEach { loggedIn ->
                val needs = loggedIn && consentRepository.needsConsent()
                _state.update { it.copy(visible = needs) }
            }
            .launchIn(viewModelScope)
    }

    fun onAgree() {
        if (_state.value.isLoading) return
        _state.update { it.copy(isLoading = true) }
        launch {
            val result = consentRepository.recordConsent()
            _state.update {
                if (result is Result.Success) {
                    it.copy(isLoading = false, visible = false)
                } else {
                    it.copy(isLoading = false)
                }
            }
        }
    }
}
