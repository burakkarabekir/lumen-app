package com.bksd.paywall.domain.model

data class PaywallConfig(
    val features: List<PremiumFeature>,
    val plans: List<SubscriptionPlan>,
    val defaultPlanId: String
)
