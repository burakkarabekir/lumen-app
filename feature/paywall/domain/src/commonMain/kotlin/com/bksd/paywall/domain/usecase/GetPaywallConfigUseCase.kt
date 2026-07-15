package com.bksd.paywall.domain.usecase

import com.bksd.paywall.domain.model.PaywallConfig
import com.bksd.paywall.domain.model.PremiumFeature

class GetPaywallConfigUseCase {

    operator fun invoke(): PaywallConfig {
        return PaywallConfig(
            features = listOf(
                PremiumFeature.MULTIMEDIA,
                PremiumFeature.AI_REFLECTION
            )
        )
    }
}
