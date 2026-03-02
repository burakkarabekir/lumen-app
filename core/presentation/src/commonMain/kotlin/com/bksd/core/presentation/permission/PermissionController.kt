package com.bksd.core.presentation.permission

import androidx.compose.runtime.Composable

/**
 * Platform-agnostic permission controller for requesting runtime permissions.
 *
 * Use [rememberPermissionController] to obtain an instance in Compose.
 */
expect class PermissionController {
    /**
     * Checks the current state of the given permission without prompting the user.
     */
    suspend fun checkPermission(permission: Permission): PermissionState

    /**
     * Requests the given permission from the user.
     * @return The resulting [PermissionState] after the request.
     */
    suspend fun requestPermission(permission: Permission): PermissionState

    /**
     * Opens the app settings page where the user can manually grant permissions.
     * Use this when permission is [PermissionState.PERMANENTLY_DENIED].
     */
    fun openAppSettings()

    /**
     * Opens the system location settings screen where the user can enable location services.
     * Use this when location permission is granted but device location services are disabled.
     */
    fun openLocationSettings()
}

/**
 * Creates and remembers a [PermissionController] instance scoped to the composition.
 */
@Composable
expect fun rememberPermissionController(): PermissionController
