package com.bksd.core.presentation.permission

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.camera.CAMERA
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import dev.icerock.moko.permissions.gallery.GALLERY
import dev.icerock.moko.permissions.location.LOCATION
import dev.icerock.moko.permissions.microphone.RECORD_AUDIO
import dev.icerock.moko.permissions.Permission as MokoPermission
import dev.icerock.moko.permissions.PermissionState as MokoPermissionState

actual class PermissionController(
    private val mokoController: PermissionsController,
    private val context: Context
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

    actual fun openLocationSettings() {
        context.startActivity(
            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}

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

private fun Permission.toMoko(): MokoPermission = when (this) {
    Permission.RECORD_AUDIO -> MokoPermission.RECORD_AUDIO
    Permission.LOCATION -> MokoPermission.LOCATION
    Permission.CAMERA -> MokoPermission.CAMERA
    Permission.GALLERY -> MokoPermission.GALLERY
}

private fun MokoPermissionState.toAppState(): PermissionState = when (this) {
    MokoPermissionState.Granted -> PermissionState.GRANTED
    MokoPermissionState.DeniedAlways -> PermissionState.PERMANENTLY_DENIED
    MokoPermissionState.Denied -> PermissionState.DENIED
    MokoPermissionState.NotDetermined -> PermissionState.NOT_DETERMINED
    MokoPermissionState.NotGranted -> PermissionState.DENIED
}
