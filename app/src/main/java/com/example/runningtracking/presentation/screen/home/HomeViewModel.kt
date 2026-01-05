package com.example.runningtracking.presentation.screen.home

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningtracking.data.service.LocationTrackerService
import com.example.runningtracking.domain.location.LocationTracker
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel(
    private val context: Context,
    private val locationTracker: LocationTracker
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.OnFetchLocation -> fetchLocation()
            HomeAction.OnToggleRunning -> toggleRunning()
        }
    }

    private fun toggleRunning() {
        val isRunning = state.value.isRunning
        val newIsRunning = !isRunning

        _state.update {
            it.copy(
                isRunning = newIsRunning,
                pathPoints = if (newIsRunning) emptyList() else it.pathPoints
            )
        }

        val intent = Intent(context, LocationTrackerService::class.java).apply {
            action = if (newIsRunning) LocationTrackerService.ACTION_START else LocationTrackerService.ACTION_STOP
        }
        context.startForegroundService(intent)
    }

    private fun fetchLocation() {
        locationTracker.observeLocation()
            .onEach { location ->
                val latLng = LatLng(location.latitude, location.longitude)
                _state.update { state ->
                    val newPathPoints = if (state.isRunning) {
                        state.pathPoints + latLng
                    } else {
                        state.pathPoints
                    }
                    state.copy(
                        location = latLng,
                        pathPoints = newPathPoints
                    )
                }
            }
            .catch { e ->
                e.printStackTrace()
            }
            .launchIn(viewModelScope)
    }
}