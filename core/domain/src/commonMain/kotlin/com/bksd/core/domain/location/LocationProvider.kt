package com.bksd.core.domain.location

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result

interface LocationProvider {
    suspend fun getCurrentLocation(): Result<LocationData, AppError>
    fun hasPermission(): Boolean
}
