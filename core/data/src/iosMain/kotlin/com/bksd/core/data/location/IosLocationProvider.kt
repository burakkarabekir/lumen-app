package com.bksd.core.data.location

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.location.LocationData
import com.bksd.core.domain.location.LocationProvider
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.CLGeocoder
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.CLPlacemark
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

@OptIn(ExperimentalForeignApi::class)
class IosLocationProvider : LocationProvider {

    private val locationManager = CLLocationManager()
    private val geocoder = CLGeocoder()

    // Strong reference to prevent Kotlin/Native GC
    private val locationDelegate = LocationDelegate()

    init {
        locationManager.delegate = locationDelegate
    }

    override fun hasPermission(): Boolean {
        val status = CLLocationManager.authorizationStatus()
        return status == kCLAuthorizationStatusAuthorizedWhenInUse ||
                status == kCLAuthorizationStatusAuthorizedAlways
    }

    override suspend fun getCurrentLocation(): Result<LocationData, AppError> {
        if (!hasPermission()) {
            return Result.Error(AppError.Unknown("Location permission denied"))
        }

        return suspendCancellableCoroutine { continuation ->
            locationDelegate.continuation = continuation
            locationManager.requestLocation()
        }
    }

    private inner class LocationDelegate : NSObject(), CLLocationManagerDelegateProtocol {
        var continuation: Continuation<Result<LocationData, AppError>>? = null

        override fun locationManager(
            manager: CLLocationManager,
            didUpdateLocations: List<*>
        ) {
            val location = didUpdateLocations.lastOrNull() as? CLLocation
            if (location != null) {
                geocoder.reverseGeocodeLocation(location) { placemarks, _ ->
                    val placemark = placemarks?.firstOrNull() as? CLPlacemark
                    val city = placemark?.locality
                    val state = placemark?.administrativeArea

                    val displayName = if (city != null && state != null) {
                        "$city, $state"
                    } else city ?: state

                    val cont = continuation
                    continuation = null

                    cont?.resume(
                        Result.Success(
                            LocationData(
                                latitude = location.coordinate.useContents { latitude },
                                longitude = location.coordinate.useContents { longitude },
                                displayName = displayName
                            )
                        )
                    )
                }
            } else {
                val cont = continuation
                continuation = null
                cont?.resume(Result.Error(AppError.Unknown("No location received")))
            }
        }

        override fun locationManager(
            manager: CLLocationManager,
            didFailWithError: NSError
        ) {
            val cont = continuation
            continuation = null
            cont?.resume(Result.Error(AppError.Unknown(didFailWithError.localizedDescription)))
        }
    }
}
