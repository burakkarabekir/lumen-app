package com.bksd.core.presentation.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.microphone.RECORD_AUDIO
import dev.icerock.moko.permissions.Permission as MokoPermission
import dev.icerock.moko.permissions.PermissionState as MokoPermissionState
import dev.icerock.moko.permissions.ios.PermissionsController as IosPermissionsController

actual class PermissionController(
    private val mokoController: IosPermissionsController
) {
    actual suspend fun checkPermission(permission: Permission): PermissionState {
        return mokoController
            .getPermissionState(permission.toMoko())
            .toAppState()
    }

    actual suspend fun requestPermission(permission: Permission): PermissionState {
        return try {
            mokoController.providePermission(permission.toMoko())
            PermissionState.GRANTED
        } catch (_: DeniedAlwaysException) {
            PermissionState.PERMANENTLY_DENIED
        } catch (_: DeniedException) {
            PermissionState.DENIED
        }
    }

    actual fun openAppSettings() {
        mokoController.openAppSettings()
    }
}

@Composable
actual fun rememberPermissionController(): PermissionController {
    return remember {
        PermissionController(IosPermissionsController())
    }
}

private fun Permission.toMoko(): MokoPermission = when (this) {
    Permission.RECORD_AUDIO -> MokoPermission.RECORD_AUDIO
}

private fun MokoPermissionState.toAppState(): PermissionState = when (this) {
    MokoPermissionState.Granted -> PermissionState.GRANTED
    MokoPermissionState.DeniedAlways -> PermissionState.PERMANENTLY_DENIED
    MokoPermissionState.Denied -> PermissionState.DENIED
    MokoPermissionState.NotDetermined -> PermissionState.NOT_DETERMINED
    MokoPermissionState.NotGranted -> PermissionState.DENIED
}
