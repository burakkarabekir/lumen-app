package com.bksd.paywall.presentation

import com.bksd.core.domain.billing.BillingPeriod
import com.bksd.core.domain.billing.BillingProduct
import com.bksd.core.domain.billing.EntitlementRepository
import com.bksd.core.domain.billing.PurchaseOutcome
import com.bksd.core.domain.error.Result
import com.bksd.core.presentation.util.BaseViewModel
import com.bksd.paywall.domain.usecase.GetPaywallConfigUseCase
import com.bksd.paywall.presentation.mapper.toUiState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PaywallViewModel(
    private val getPaywallConfigUseCase: GetPaywallConfigUseCase,
    private val entitlementRepository: EntitlementRepository,
) : BaseViewModel<PaywallAction, PaywallEvent>() {

    private val _stateFlow = MutableStateFlow(createInitialState())
    val state: StateFlow<PaywallState> = _stateFlow.asStateFlow()

    init {
        loadProducts()
    }

    override fun onAction(action: PaywallAction) {
        when (action) {
            is PaywallAction.OnSelectTier ->
                _stateFlow.update { it.copy(selectedTier = action.tier) }

            PaywallAction.OnSubscribeClick -> handleSubscribe()
            PaywallAction.OnCloseClick -> sendEvent(PaywallEvent.Dismiss)
            PaywallAction.OnRestoreClick -> handleRestore()
        }
    }

    private fun loadProducts() {
        launch {
            val result = entitlementRepository.products()
            if (result is Result.Success && result.data.isNotEmpty()) {
                val tiers = result.data.map { it.toTierUi() }.toImmutableList()
                _stateFlow.update {
                    it.copy(
                        tiers = tiers,
                        selectedTier = tiers.firstOrNull { tier -> tier.badge == PaywallBadge.BEST_VALUE }
                            ?: tiers.first(),
                    )
                }
            }
        }
    }

    private fun handleSubscribe() {
        val tier = _stateFlow.value.selectedTier ?: return
        _stateFlow.update { it.copy(isProcessing = true) }
        launch {
            val outcome = entitlementRepository.purchase(tier.id)
            _stateFlow.update { it.copy(isProcessing = false) }
            when (outcome) {
                PurchaseOutcome.Success -> {
                    sendEvent(PaywallEvent.SubscriptionSuccess)
                    sendEvent(PaywallEvent.Dismiss)
                }

                PurchaseOutcome.Cancelled -> Unit
                is PurchaseOutcome.Failed -> Unit
            }
        }
    }

    private fun handleRestore() {
        _stateFlow.update { it.copy(isProcessing = true) }
        launch {
            val outcome = entitlementRepository.restore()
            val isPlus = entitlementRepository.currentEntitlement().isPlus
            _stateFlow.update { it.copy(isProcessing = false) }
            if (outcome is PurchaseOutcome.Success && isPlus) {
                sendEvent(PaywallEvent.SubscriptionSuccess)
                sendEvent(PaywallEvent.Dismiss)
            }
        }
    }

    private fun createInitialState(): PaywallState = getPaywallConfigUseCase().toUiState()
}

private fun BillingProduct.toTierUi(): BillingTierUi = BillingTierUi(
    id = id,
    displayName = when (period) {
        BillingPeriod.YEARLY -> "Yearly"
        BillingPeriod.MONTHLY -> "Monthly"
        BillingPeriod.WEEKLY -> "Weekly"
        BillingPeriod.LIFETIME -> "Lifetime"
        BillingPeriod.UNKNOWN -> title
    },
    price = priceLabel,
    period = when (period) {
        BillingPeriod.YEARLY -> "per year"
        BillingPeriod.MONTHLY -> "per month"
        BillingPeriod.WEEKLY -> "per week"
        else -> ""
    },
    subtitle = when {
        hasFreeTrial -> "5-day free trial"
        period == BillingPeriod.YEARLY -> "Billed annually"
        period == BillingPeriod.WEEKLY -> "Billed weekly"
        period == BillingPeriod.LIFETIME -> "One-time purchase"
        else -> "Billed monthly"
    },
    monthlyBreakdown = null,
    badge = when (period) {
        BillingPeriod.YEARLY -> PaywallBadge.BEST_VALUE
        BillingPeriod.MONTHLY -> PaywallBadge.POPULAR
        else -> null
    },
    hasFreeTrial = hasFreeTrial,
)
