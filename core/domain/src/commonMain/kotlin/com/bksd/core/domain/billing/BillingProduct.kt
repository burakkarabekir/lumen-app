package com.bksd.core.domain.billing

data class BillingProduct(
    val id: String,
    val title: String,
    val priceLabel: String,
    val period: BillingPeriod,
    val hasFreeTrial: Boolean,
)

enum class BillingPeriod {
    WEEKLY,
    MONTHLY,
    YEARLY,
    LIFETIME,
    UNKNOWN,
}
