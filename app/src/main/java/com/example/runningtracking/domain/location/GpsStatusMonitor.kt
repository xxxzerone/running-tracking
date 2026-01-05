package com.example.runningtracking.domain.location

import kotlinx.coroutines.flow.Flow

interface GpsStatusMonitor {
    val isGpsEnabled: Flow<Boolean>
}