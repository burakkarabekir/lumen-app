package com.bksd.paywall.domain.model

data class SubscriptionPlan(
    val id: String,
    val name: String,
    val price: String,
    val period: BillingPeriod,
    val subtitle: String,
    val monthlyEquivalent: String? = null,
    val isPopularChoice: Boolean = false,
    val hasFreeTrial: Boolean = false
)

enum class BillingPeriod {
    MONTHLY,
    YEARLY
}
