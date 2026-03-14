package com.bksd.paywall.domain.usecase

import com.bksd.paywall.domain.model.BillingPeriod
import com.bksd.paywall.domain.model.PaywallConfig
import com.bksd.paywall.domain.model.PremiumFeature
import com.bksd.paywall.domain.model.SubscriptionPlan

class GetPaywallConfigUseCase {

    operator fun invoke(): PaywallConfig {
        return PaywallConfig(
            features = listOf(
                PremiumFeature(
                    title = "Unlimited Multimedia",
                    description = "Rich entries with high-resolution photos and voice recordings."
                ),
                PremiumFeature(
                    title = "AI Weekly Summaries",
                    description = "Personalized reflection insights delivered every Sunday morning."
                ),
                PremiumFeature(
                    title = "Advanced Analytics",
                    description = "Visualize your emotional journey and mood trends over time."
                )
            ),
            plans = listOf(
                SubscriptionPlan(
                    id = "yearly",
                    name = "Yearly Access",
                    price = "$79.99",
                    period = BillingPeriod.YEARLY,
                    subtitle = "7-DAY FREE TRIAL",
                    monthlyEquivalent = "$6.66 per month",
                    isPopularChoice = true,
                    hasFreeTrial = true
                ),
                SubscriptionPlan(
                    id = "monthly",
                    name = "Monthly",
                    price = "$9.99",
                    period = BillingPeriod.MONTHLY,
                    subtitle = "Standard access"
                )
            ),
            defaultPlanId = "yearly"
        )
    }
}
