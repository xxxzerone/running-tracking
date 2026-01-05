package com.example.runningtracking.data.location

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import com.example.runningtracking.domain.location.LocationTracker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class DefaultLocationTracker(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : LocationTracker {

    @SuppressLint("MissingPermission")
    override fun observeLocation(): Flow<Location> {
        return callbackFlow {
            try {
                val lastLocation = fusedLocationProviderClient.lastLocation.await()
                lastLocation?.let { trySend(it) }
            } catch (e: Exception) {
                // Ignore failure to get last location
            }

            val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000L)
                .setMinUpdateIntervalMillis(1000L)
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    result.locations.lastOrNull()?.let { location ->
                        trySend(location)
                    }
                }
            }

            try {
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            } catch (e: SecurityException) {
                close(e)
            }

            awaitClose {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Location? {
        return try {
            fusedLocationProviderClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            ).await()
        } catch (e: Exception) {
            null
        }
    }
}