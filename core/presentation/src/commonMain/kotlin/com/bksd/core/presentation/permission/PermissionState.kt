package com.bksd.core.presentation.permission

/**
 * Represents the current state of a permission.
 */
enum class PermissionState {
    /** Permission has been granted by the user */
    GRANTED,

    /** Permission was denied but can be requested again */
    DENIED,

    /** Permission was permanently denied; user must go to settings */
    PERMANENTLY_DENIED,

    /** Permission hasn't been requested yet (iOS-specific) */
    NOT_DETERMINED;

    val isGranted: Boolean get() = this == GRANTED

    val canRequest: Boolean get() = this == NOT_DETERMINED || this == DENIED
}
