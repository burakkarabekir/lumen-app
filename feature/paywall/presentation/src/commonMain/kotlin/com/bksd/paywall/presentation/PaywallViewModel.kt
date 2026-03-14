package com.bksd.paywall.presentation

import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.paywall.domain.usecase.GetPaywallConfigUseCase
import com.bksd.paywall.presentation.mapper.toUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PaywallViewModel(
    private val getPaywallConfigUseCase: GetPaywallConfigUseCase
) : BaseViewModel<PaywallAction, PaywallEvent>() {

    private val _stateFlow = MutableStateFlow(createInitialState())
    val state: StateFlow<PaywallState> = _stateFlow.asStateFlow()

    override fun onAction(action: PaywallAction) {
        when (action) {
            is PaywallAction.OnSelectTier -> {
                _stateFlow.update { it.copy(selectedTier = action.tier) }
            }
            PaywallAction.OnSubscribeClick -> handleSubscribe()
            PaywallAction.OnCloseClick -> sendEvent(PaywallEvent.Dismiss)
            PaywallAction.OnRestoreClick -> Unit
        }
    }

    private fun handleSubscribe() {
        _stateFlow.update { it.copy(isProcessing = true) }
        launch {
            _stateFlow.update { it.copy(isProcessing = false) }
            sendEvent(PaywallEvent.Dismiss)
        }
    }

    private fun createInitialState(): PaywallState {
        return getPaywallConfigUseCase().toUiState()
    }
}
