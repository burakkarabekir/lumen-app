package com.bksd.core.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.LocationErrorType
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.location.LocationData
import com.bksd.core.domain.location.LocationProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
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

    private val locationManager: LocationManager by lazy {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private fun isLocationEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            locationManager.isLocationEnabled
        } else {
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            isGpsEnabled || isNetworkEnabled
        }
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
        if (!isLocationEnabled()) {
            return Result.Error(AppError.Location(LocationErrorType.SERVICES_DISABLED))
        }

        if (!hasPermission()) {
            return Result.Error(AppError.Location(LocationErrorType.PERMISSION_DENIED))
        }

        val location = requestCurrentLocation() ?: requestLastLocation()

        if (location == null) {
            return Result.Error(AppError.Location(LocationErrorType.UNAVAILABLE))
        }

        val displayName = getCityState(location.latitude, location.longitude)
        return Result.Success(
            LocationData(
                latitude = location.latitude,
                longitude = location.longitude,
                displayName = displayName
            )
        )
    }

    @SuppressLint("MissingPermission")
    private suspend fun requestCurrentLocation(): Location? {
        return suspendCancellableCoroutine { continuation ->
            val cancellationTokenSource = CancellationTokenSource()

            continuation.invokeOnCancellation {
                cancellationTokenSource.cancel()
            }

            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            ).addOnSuccessListener { location: Location? ->
                continuation.resume(location)
            }.addOnFailureListener {
                continuation.resume(null)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun requestLastLocation(): Location? {
        return suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    continuation.resume(location)
                }
                .addOnFailureListener {
                    continuation.resume(null)
                }
        }
    }

    private suspend fun getCityState(lat: Double, lon: Double): String? {
        return withContext(Dispatchers.IO) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    suspendCancellableCoroutine { cont ->
                        geocoder.getFromLocation(lat, lon, 1) { addresses ->
                            cont.resume(formatAddress(addresses))
                        }
                    }
                } else {
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocation(lat, lon, 1)
                    formatAddress(addresses.orEmpty())
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun formatAddress(addresses: List<android.location.Address>): String? {
        if (addresses.isEmpty()) return null
        val address = addresses[0]
        val city = address.locality ?: address.subAdminArea
        val state = address.adminArea
        return when {
            city != null && state != null -> "$city, $state"
            else -> city ?: state
        }
    }
}
