package com.example.runningtracking.presentation.screen.home

import com.google.android.gms.maps.model.LatLng

data class HomeState(
    val location: LatLng? = null,
    val isRunning: Boolean = false,
    val pathPoints: List<LatLng> = emptyList()
)