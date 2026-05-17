package com.bksd.core.presentation.permission

import androidx.compose.runtime.Composable

/**
 * Creates and remembers a [PermissionController] instance scoped to the composition.
 */
@Composable
expect fun rememberPermissionController(): PermissionController
