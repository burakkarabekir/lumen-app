package com.bksd.core.domain.billing

interface PremiumStatusSource {
    suspend fun isServerPremium(): Boolean
}
