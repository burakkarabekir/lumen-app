package com.bksd.core.domain.consent

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result

interface ConsentRepository {
    suspend fun needsConsent(): Boolean
    suspend fun recordConsent(): Result<Unit, AppError>
}
