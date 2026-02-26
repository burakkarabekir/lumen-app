package com.bksd.paywall.presentation

import com.bksd.core.presentation.util.BaseViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for the Momentum Premium Paywall screen.
 * Handles tier selection and subscription flow (fake for Phase 2).
 */
class PaywallViewModel : BaseViewModel<PaywallAction, PaywallEvent>() {

    private val _stateFlow = MutableStateFlow(createInitialState())
    val state: StateFlow<PaywallState> = _stateFlow.asStateFlow()

    override fun onAction(action: PaywallAction) {
        when (action) {
            is PaywallAction.OnSelectTier -> {
                _stateFlow.update { it.copy(selectedTier = action.tier) }
            }

            PaywallAction.OnSubscribeClick -> handleSubscribe()
            PaywallAction.OnCloseClick -> sendEvent(PaywallEvent.Dismiss)
            PaywallAction.OnRestoreClick -> {
                // No-op for Phase 2
            }
        }
    }

    private fun handleSubscribe() {
        _stateFlow.update { it.copy(isProcessing = true) }
        launch {
            // In Phase 3, this will trigger in-app purchase flow
            _stateFlow.update { it.copy(isProcessing = false) }
            sendEvent(PaywallEvent.Dismiss)
        }
    }

    private fun createInitialState(): PaywallState = PaywallState(
        features = persistentListOf(
            PaywallFeature(
                title = "Unlimited Multimedia",
                description = "Add photos, voice notes, and videos to any entry."
            ),
            PaywallFeature(
                title = "AI Weekly Summaries",
                description = "Get automated insights from your past week."
            ),
            PaywallFeature(
                title = "Advanced Analytics",
                description = "Track mood patterns and habits over time."
            )
        ),
        selectedTier = BillingTier.YEARLY,
        isProcessing = false
    )
}
