package com.bksd.core.presentation.permission

import androidx.compose.runtime.Composable

/**
 * Platform-agnostic interface for resolving device-level location services.
 *
 * On Android, this shows the native Google Play Services "Turn on Location" dialog.
 * On iOS, this is a no-op (returns true) since iOS handles this via CLLocationManager.
 */
interface LocationSettingsResolver {
    /**
     * Ensures device location services are enabled.
     * On Android, shows the native system dialog if needed.
     *
     * @return true if location services are now enabled, false if user declined.
     */
    suspend fun ensureLocationEnabled(): Boolean
}

/**
 * Creates and remembers a [LocationSettingsResolver] instance scoped to the composition.
 */
@Composable
expect fun rememberLocationSettingsResolver(): LocationSettingsResolver
