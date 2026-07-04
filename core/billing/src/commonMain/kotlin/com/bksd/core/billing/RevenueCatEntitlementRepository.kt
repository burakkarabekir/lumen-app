package com.bksd.core.billing

import com.bksd.core.domain.billing.BillingPeriod
import com.bksd.core.domain.billing.BillingProduct
import com.bksd.core.domain.billing.Entitlement
import com.bksd.core.domain.billing.EntitlementRepository
import com.bksd.core.domain.billing.PremiumStatusSource
import com.bksd.core.domain.billing.PurchaseOutcome
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.ktx.awaitCustomerInfo
import com.revenuecat.purchases.kmp.ktx.awaitLogIn
import com.revenuecat.purchases.kmp.ktx.awaitLogOut
import com.revenuecat.purchases.kmp.ktx.awaitOfferings
import com.revenuecat.purchases.kmp.ktx.awaitPurchase
import com.revenuecat.purchases.kmp.ktx.awaitRestore
import com.revenuecat.purchases.kmp.models.CustomerInfo
import com.revenuecat.purchases.kmp.models.Package
import com.revenuecat.purchases.kmp.models.PackageType
import com.revenuecat.purchases.kmp.models.PurchasesTransactionException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

class RevenueCatEntitlementRepository(
    private val premiumStatusSource: PremiumStatusSource,
) : EntitlementRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 1, extraBufferCapacity = 1)
    private var cachedPackages: Map<String, Package> = emptyMap()

    init {
        refreshTrigger.tryEmit(Unit)
    }

    override val entitlement: Flow<Entitlement> =
        refreshTrigger
            .map { currentEntitlement() }
            .distinctUntilChanged()
            .shareIn(scope, SharingStarted.WhileSubscribed(5_000), replay = 1)

    override fun refresh() {
        refreshTrigger.tryEmit(Unit)
    }

    override suspend fun currentEntitlement(): Entitlement {
        val rcPlus = if (Purchases.isConfigured) {
            runCatching { Purchases.sharedInstance.awaitCustomerInfo().toEntitlement().isPlus }
                .getOrDefault(false)
        } else {
            false
        }
        val serverPlus = runCatching { premiumStatusSource.isServerPremium() }.getOrDefault(false)
        return Entitlement(isPlus = rcPlus || serverPlus)
    }

    override suspend fun products(): Result<List<BillingProduct>, AppError> = try {
        val offering = Purchases.sharedInstance.awaitOfferings().current
        val packages = offering?.availablePackages.orEmpty()
        cachedPackages = packages.associateBy { it.storeProduct.id }
        Result.Success(packages.map { it.toBillingProduct() })
    } catch (e: Throwable) {
        Result.Error(AppError.Unknown(e.message ?: "offerings_failed"))
    }

    override suspend fun purchase(productId: String): PurchaseOutcome {
        val target = cachedPackages[productId] ?: run {
            products()
            cachedPackages[productId]
        } ?: return PurchaseOutcome.Failed("product_not_found")
        return try {
            Purchases.sharedInstance.awaitPurchase(target)
            refresh()
            PurchaseOutcome.Success
        } catch (e: PurchasesTransactionException) {
            if (e.userCancelled) PurchaseOutcome.Cancelled
            else PurchaseOutcome.Failed(e.message ?: "purchase_failed")
        }
    }

    override suspend fun restore(): PurchaseOutcome = try {
        Purchases.sharedInstance.awaitRestore()
        refresh()
        PurchaseOutcome.Success
    } catch (e: Throwable) {
        PurchaseOutcome.Failed(e.message ?: "restore_failed")
    }

    override suspend fun setUser(userId: String) {
        runCatching { Purchases.sharedInstance.awaitLogIn(userId) }
        refresh()
    }

    override suspend fun clearUser() {
        runCatching { Purchases.sharedInstance.awaitLogOut() }
        refresh()
    }
}

private fun CustomerInfo.toEntitlement(): Entitlement =
    Entitlement(isPlus = entitlements.active.isNotEmpty())

private fun Package.toBillingProduct(): BillingProduct = BillingProduct(
    id = storeProduct.id,
    title = storeProduct.title,
    priceLabel = storeProduct.price.formatted,
    period = packageType.toBillingPeriod(),
    hasFreeTrial = false,
)

private fun PackageType.toBillingPeriod(): BillingPeriod = when (this) {
    PackageType.WEEKLY -> BillingPeriod.WEEKLY
    PackageType.MONTHLY -> BillingPeriod.MONTHLY
    PackageType.ANNUAL -> BillingPeriod.YEARLY
    PackageType.LIFETIME -> BillingPeriod.LIFETIME
    else -> BillingPeriod.UNKNOWN
}
