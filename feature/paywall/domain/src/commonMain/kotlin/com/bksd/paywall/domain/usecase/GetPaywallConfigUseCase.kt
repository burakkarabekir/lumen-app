package com.bksd.paywall.domain.usecase

import com.bksd.paywall.domain.model.BillingPeriod
import com.bksd.paywall.domain.model.FeatureIcon
import com.bksd.paywall.domain.model.PaywallConfig
import com.bksd.paywall.domain.model.PlanBadge
import com.bksd.paywall.domain.model.PremiumFeature
import com.bksd.paywall.domain.model.SubscriptionPlan

class GetPaywallConfigUseCase {

    operator fun invoke(): PaywallConfig {
        return PaywallConfig(
            features = listOf(
                PremiumFeature(
                    title = "Unlimited Multimedia",
                    description = "Enrich entries with high-resolution photos, video, and voice recordings.",
                    icon = FeatureIcon.MULTIMEDIA
                ),
                PremiumFeature(
                    title = "AI Weekly Reflections",
                    description = "Personal, written insights on your moods and themes every Sunday.",
                    icon = FeatureIcon.AI_REFLECTION
                ),
                PremiumFeature(
                    title = "Advanced Analytics",
                    description = "Visualize your emotional journey and mood trends over time.",
                    icon = FeatureIcon.ANALYTICS
                )
            ),
            plans = listOf(
                SubscriptionPlan(
                    id = "yearly",
                    name = "Yearly",
                    price = "$79.99",
                    period = BillingPeriod.YEARLY,
                    subtitle = "5-day free trial · $6.66/mo",
                    badge = PlanBadge.BEST_VALUE,
                    hasFreeTrial = true
                ),
                SubscriptionPlan(
                    id = "monthly",
                    name = "Monthly",
                    price = "$9.99",
                    period = BillingPeriod.MONTHLY,
                    subtitle = "Billed monthly",
                    badge = PlanBadge.POPULAR
                )
            ),
            defaultPlanId = "yearly"
        )
    }
}
