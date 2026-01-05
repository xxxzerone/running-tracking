package com.example.runningtracking.domain.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationTracker {
    fun observeLocation(): Flow<Location>
    suspend fun getCurrentLocation(): Location?
}