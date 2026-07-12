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
import org.jetbrains.compose.resources.getString

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
            PaywallAction.OnRetryLoad -> loadProducts()
        }
    }

    private fun loadProducts() {
        launch {
            _stateFlow.update { it.copy(isLoading = true, loadError = false) }
            when (val result = entitlementRepository.products()) {
                is Result.Success -> {
                    val tiers = result.data.map { it.toTierUi() }.toImmutableList()
                    _stateFlow.update {
                        it.copy(
                            isLoading = false,
                            loadError = tiers.isEmpty(),
                            tiers = tiers,
                            selectedTier = tiers.firstOrNull { tier -> tier.badge == PaywallBadge.BEST_VALUE }
                                ?: tiers.firstOrNull(),
                        )
                    }
                }

                is Result.Error -> _stateFlow.update { it.copy(isLoading = false, loadError = true) }
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
                is PurchaseOutcome.Failed -> sendEvent(PaywallEvent.ShowError(outcome.message))
            }
        }
    }

    private fun handleRestore() {
        _stateFlow.update { it.copy(isProcessing = true) }
        launch {
            val outcome = entitlementRepository.restore()
            val isPlus = entitlementRepository.currentEntitlement().isPlus
            _stateFlow.update { it.copy(isProcessing = false) }
            when (outcome) {
                is PurchaseOutcome.Success ->
                    if (isPlus) {
                        sendEvent(PaywallEvent.SubscriptionSuccess)
                        sendEvent(PaywallEvent.Dismiss)
                    } else {
                        sendEvent(PaywallEvent.RestoreNone)
                    }

                PurchaseOutcome.Cancelled -> Unit
                is PurchaseOutcome.Failed -> sendEvent(PaywallEvent.ShowError(outcome.message))
            }
        }
    }

    private fun createInitialState(): PaywallState = getPaywallConfigUseCase().toUiState()
}

private suspend fun BillingProduct.toTierUi(): BillingTierUi = BillingTierUi(
    id = id,
    displayName = when (period) {
        BillingPeriod.YEARLY -> getString(Res.string.tier_name_yearly)
        BillingPeriod.MONTHLY -> getString(Res.string.tier_name_monthly)
        BillingPeriod.WEEKLY -> getString(Res.string.tier_name_weekly)
        BillingPeriod.LIFETIME -> getString(Res.string.tier_name_lifetime)
        BillingPeriod.UNKNOWN -> title
    },
    price = priceLabel,
    period = when (period) {
        BillingPeriod.YEARLY -> getString(Res.string.tier_period_yearly)
        BillingPeriod.MONTHLY -> getString(Res.string.tier_period_monthly)
        BillingPeriod.WEEKLY -> getString(Res.string.tier_period_weekly)
        else -> ""
    },
    subtitle = when {
        hasFreeTrial -> getString(Res.string.tier_free_trial)
        period == BillingPeriod.YEARLY -> getString(Res.string.tier_billed_annually)
        period == BillingPeriod.WEEKLY -> getString(Res.string.tier_billed_weekly)
        period == BillingPeriod.LIFETIME -> getString(Res.string.tier_one_time)
        else -> getString(Res.string.tier_billed_monthly)
    },
    monthlyBreakdown = null,
    badge = when (period) {
        BillingPeriod.YEARLY -> PaywallBadge.BEST_VALUE
        BillingPeriod.MONTHLY -> PaywallBadge.POPULAR
        else -> null
    },
    hasFreeTrial = hasFreeTrial,
)
