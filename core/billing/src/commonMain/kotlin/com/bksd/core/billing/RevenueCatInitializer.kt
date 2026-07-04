package com.bksd.core.billing

import com.revenuecat.purchases.kmp.LogLevel
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.configure

object RevenueCatInitializer {
    fun configure(appUserId: String? = null) {
        if (Purchases.isConfigured) return
        Purchases.logLevel = LogLevel.INFO
        Purchases.configure(apiKey = revenueCatApiKey) {
            this.appUserId = appUserId
        }
    }
}
