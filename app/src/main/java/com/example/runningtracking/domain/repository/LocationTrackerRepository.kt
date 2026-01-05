package com.example.runningtracking.domain.repository

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationTrackerRepository {
    fun observeLocation(): Flow<Location>
    suspend fun getCurrentLocation(): Location?
}