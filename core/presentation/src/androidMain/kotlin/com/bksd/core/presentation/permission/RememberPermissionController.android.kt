package com.bksd.core.presentation.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory

@Composable
actual fun rememberPermissionController(): PermissionController {
    val factory = rememberPermissionsControllerFactory()
    val context = LocalContext.current
    val mokoController = remember {
        factory.createPermissionsController()
    }

    BindEffect(mokoController)

    return remember {
        PermissionController(mokoController, context)
    }
}
