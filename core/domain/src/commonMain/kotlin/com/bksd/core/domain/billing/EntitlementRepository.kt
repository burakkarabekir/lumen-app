package com.bksd.core.domain.billing

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import kotlinx.coroutines.flow.Flow

interface EntitlementRepository {
    val entitlement: Flow<Entitlement>
    fun refresh()
    suspend fun currentEntitlement(): Entitlement
    suspend fun products(): Result<List<BillingProduct>, AppError>
    suspend fun purchase(productId: String): PurchaseOutcome
    suspend fun restore(): PurchaseOutcome
    suspend fun setUser(userId: String)
    suspend fun clearUser()
}
