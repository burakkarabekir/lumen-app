package com.bksd.core.presentation.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.icerock.moko.permissions.ios.PermissionsController as IosPermissionsController

@Composable
actual fun rememberPermissionController(): PermissionController {
    return remember {
        PermissionController(IosPermissionsController())
    }
}
