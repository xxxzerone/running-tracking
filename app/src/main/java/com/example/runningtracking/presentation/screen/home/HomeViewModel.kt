package com.example.runningtracking.presentation.screen.home

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningtracking.data.service.LocationTrackerService
import com.example.runningtracking.domain.location.GpsStatusMonitor
import com.example.runningtracking.domain.location.LocationTracker
import com.example.runningtracking.domain.model.Run
import com.example.runningtracking.domain.power.BatteryMonitor
import com.example.runningtracking.domain.repository.RunRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel(
    private val context: Context,
    private val locationTracker: LocationTracker,
    private val runRepository: RunRepository,
    private val gpsStatusMonitor: GpsStatusMonitor,
    private val batteryMonitor: BatteryMonitor
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _event = Channel<HomeEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    private var batteryObserverJob: Job? = null

    init {
        gpsStatusMonitor.isGpsEnabled
            .onEach { isEnabled ->
                _state.update { it.copy(isGpsEnabled = isEnabled) }
                if (!isEnabled) {
                    _event.send(HomeEvent.GpsDisabled)
                }
            }
            .launchIn(viewModelScope)

        runRepository.getAllRuns()
            .onEach { runs ->
                _state.update { it.copy(runLogs = runs) }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.OnFetchLocation -> fetchLocation()
            HomeAction.OnToggleRunning -> toggleRunning()
            HomeAction.OnToggleRunLogs -> {
                _state.update { it.copy(isRunLogsVisible = !it.isRunLogsVisible) }
            }
        }
    }

    private fun toggleRunning() {
        val isRunning = state.value.isRunning
        val newIsRunning = !isRunning

        if (newIsRunning) {
            startRun()
        } else {
            stopRun()
        }
    }

    private fun startRun() {
        val batteryLevel = batteryMonitor.getBatteryLevel()
        if (batteryLevel <= 30) {
            viewModelScope.launch {
                _event.send(HomeEvent.BatteryLow)
            }
        }

        _state.update {
            it.copy(
                isRunning = true,
                pathPoints = emptyList(),
                startTime = System.currentTimeMillis()
            )
        }
        
        startBatteryMonitoring()
        
        val intent = Intent(context, LocationTrackerService::class.java).apply {
            action = LocationTrackerService.ACTION_START
        }
        context.startForegroundService(intent)
    }

    private fun stopRun(isLowBattery: Boolean = false) {
        saveRun()
        stopBatteryMonitoring()
        
        _state.update {
            it.copy(isRunning = false)
        }

        val intent = Intent(context, LocationTrackerService::class.java).apply {
            action = LocationTrackerService.ACTION_STOP
        }
        context.startForegroundService(intent)

        if (isLowBattery) {
            viewModelScope.launch {
                _event.send(HomeEvent.RunFinishedLowBattery)
            }
        }
    }

    private fun startBatteryMonitoring() {
        batteryObserverJob?.cancel()
        batteryObserverJob = batteryMonitor.observeBatteryLevel()
            .onEach { level ->
                if (level <= 20) {
                    stopRun(isLowBattery = true)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun stopBatteryMonitoring() {
        batteryObserverJob?.cancel()
        batteryObserverJob = null
    }

    private fun saveRun() {
        val currentState = state.value
        val endTime = System.currentTimeMillis()
        val duration = endTime - currentState.startTime
        val distance = calculateDistance(currentState.pathPoints)

        viewModelScope.launch(Dispatchers.IO) {
            runRepository.insertRun(
                Run(
                    timestamp = currentState.startTime,
                    durationMillis = duration,
                    distanceMeters = distance
                )
            )
        }
    }

    private fun calculateDistance(points: List<LatLng>): Int {
        var distance = 0f
        for (i in 0 until points.size - 1) {
            val results = FloatArray(1)
            Location.distanceBetween(
                points[i].latitude, points[i].longitude,
                points[i+1].latitude, points[i+1].longitude,
                results
            )
            distance += results[0]
        }
        return distance.toInt()
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