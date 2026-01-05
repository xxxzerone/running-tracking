package com.example.runningtracking.presentation.screen.home

import com.example.runningtracking.domain.model.Run
import com.google.android.gms.maps.model.LatLng

data class HomeState(
    val location: LatLng? = null,
    val isRunning: Boolean = false,
    val pathPoints: List<LatLng> = emptyList(),
    val startTime: Long = 0L,
    val runLogs: List<Run> = emptyList(),
    val isRunLogsVisible: Boolean = false
)