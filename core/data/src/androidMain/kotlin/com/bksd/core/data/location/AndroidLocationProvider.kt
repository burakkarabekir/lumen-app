package com.bksd.core.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.core.content.ContextCompat
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.location.LocationData
import com.bksd.core.domain.location.LocationProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume

class AndroidLocationProvider(
    private val context: Context
) : LocationProvider {

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    private val geocoder: Geocoder by lazy {
        Geocoder(context, Locale.getDefault())
    }

    override fun hasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Result<LocationData, AppError> {
        if (!hasPermission()) {
            return Result.Error(AppError.Unknown("Location permission denied"))
        }

        return suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val displayName = getCityState(location.latitude, location.longitude)
                        continuation.resume(
                            Result.Success(
                                LocationData(
                                    latitude = location.latitude,
                                    longitude = location.longitude,
                                    displayName = displayName
                                )
                            )
                        )
                    } else {
                        continuation.resume(Result.Error(AppError.Unknown("Location unavailable")))
                    }
                }
                .addOnFailureListener { e ->
                    continuation.resume(
                        Result.Error(
                            AppError.Unknown(
                                e.message ?: "Location error"
                            )
                        )
                    )
                }
        }
    }

    private fun getCityState(lat: Double, lon: Double): String? {
        return try {
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val city = address.locality ?: address.subAdminArea
                val state = address.adminArea
                if (city != null && state != null) {
                    "$city, $state"
                } else city ?: state
            } else null
        } catch (e: Exception) {
            null
        }
    }
}
