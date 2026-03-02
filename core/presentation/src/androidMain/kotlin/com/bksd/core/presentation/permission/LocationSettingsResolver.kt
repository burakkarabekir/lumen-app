package com.bksd.core.presentation.permission

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Android implementation of [LocationSettingsResolver] using Google Play Services
 * [LocationSettingsRequest] to show the native "Turn on Location" system dialog.
 */
private class AndroidLocationSettingsResolver(
    private val context: android.content.Context,
    private val launcher: ActivityResultLauncher<IntentSenderRequest>
) : LocationSettingsResolver {

    private var pendingContinuation: kotlin.coroutines.Continuation<Boolean>? = null

    override suspend fun ensureLocationEnabled(): Boolean {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            10_000L
        ).build()

        val settingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)
            .build()

        val settingsClient = LocationServices.getSettingsClient(context)

        return suspendCancellableCoroutine { continuation ->
            settingsClient.checkLocationSettings(settingsRequest)
                .addOnSuccessListener {
                    // Location settings are already satisfied
                    continuation.resume(true)
                }
                .addOnFailureListener { exception ->
                    if (exception is ResolvableApiException) {
                        // Location settings not satisfied — show native dialog
                        pendingContinuation = continuation
                        try {
                            val intentSenderRequest = IntentSenderRequest.Builder(
                                exception.resolution.intentSender
                            ).build()
                            launcher.launch(intentSenderRequest)
                        } catch (e: Exception) {
                            pendingContinuation = null
                            continuation.resume(false)
                        }
                    } else {
                        // Cannot resolve
                        continuation.resume(false)
                    }
                }
        }
    }

    fun onResult(resultCode: Int) {
        val enabled = resultCode == Activity.RESULT_OK
        pendingContinuation?.resume(enabled)
        pendingContinuation = null
    }
}

@Composable
actual fun rememberLocationSettingsResolver(): LocationSettingsResolver {
    val context = LocalContext.current

    val resolverHolder = remember { arrayOfNulls<AndroidLocationSettingsResolver>(1) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        resolverHolder[0]?.onResult(result.resultCode)
    }

    val resolver = remember {
        AndroidLocationSettingsResolver(
            context = context,
            launcher = launcher
        ).also { resolverHolder[0] = it }
    }

    return resolver
}
