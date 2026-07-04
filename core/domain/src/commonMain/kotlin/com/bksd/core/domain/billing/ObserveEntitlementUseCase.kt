package com.bksd.core.domain.billing

import kotlinx.coroutines.flow.Flow

class ObserveEntitlementUseCase(
    private val repository: EntitlementRepository,
) {
    operator fun invoke(): Flow<Entitlement> = repository.entitlement
}
