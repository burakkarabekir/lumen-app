package com.bksd.profile.presentation

import androidx.lifecycle.viewModelScope
import com.bksd.core.domain.billing.EntitlementRepository
import com.bksd.core.domain.billing.ObserveEntitlementUseCase
import com.bksd.core.domain.billing.PurchaseOutcome
import com.bksd.core.presentation.util.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ManagePremiumViewModel(
    private val observeEntitlement: ObserveEntitlementUseCase,
    private val entitlementRepository: EntitlementRepository,
) : BaseViewModel<ManagePremiumAction, ManagePremiumEvent>() {

    private var hasLoaded = false

    private val _state = MutableStateFlow(ManagePremiumState())
    val state = _state
        .onStart {
            if (!hasLoaded) {
                hasLoaded = true
                observePlan()
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ManagePremiumState(),
        )

    private fun observePlan() {
        launch {
            observeEntitlement().collect { entitlement ->
                _state.update { it.copy(isPlus = entitlement.isPlus) }
            }
        }
    }

    override fun onAction(action: ManagePremiumAction) {
        when (action) {
            ManagePremiumAction.OnUpgradeClick -> sendEvent(ManagePremiumEvent.NavigateToPaywall)
            ManagePremiumAction.OnManageSubscriptionClick ->
                sendEvent(ManagePremiumEvent.OpenManageSubscriptions)

            ManagePremiumAction.OnRestoreClick -> handleRestore()
        }
    }

    private fun handleRestore() {
        if (_state.value.isRestoring) return
        _state.update { it.copy(isRestoring = true) }
        launch {
            val outcome = entitlementRepository.restore()
            _state.update { it.copy(isRestoring = false) }
            when (outcome) {
                is PurchaseOutcome.Success -> {
                    val isPlus = entitlementRepository.currentEntitlement().isPlus
                    sendEvent(
                        if (isPlus) ManagePremiumEvent.RestoreSuccess
                        else ManagePremiumEvent.RestoreNone
                    )
                }

                is PurchaseOutcome.Cancelled -> Unit
                is PurchaseOutcome.Failed -> sendEvent(ManagePremiumEvent.RestoreError(outcome.message))
            }
        }
    }
}
