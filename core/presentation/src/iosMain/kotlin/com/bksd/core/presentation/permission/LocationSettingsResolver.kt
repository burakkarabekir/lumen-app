package com.bksd.core.presentation.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * iOS implementation of [LocationSettingsResolver].
 * On iOS, there is no system-level resolution dialog for location services.
 * CLLocationManager handles this automatically. Returns true (no-op).
 */
private class IosLocationSettingsResolver : LocationSettingsResolver {
    override suspend fun ensureLocationEnabled(): Boolean = true
}

@Composable
actual fun rememberLocationSettingsResolver(): LocationSettingsResolver {
    return remember { IosLocationSettingsResolver() }
}
