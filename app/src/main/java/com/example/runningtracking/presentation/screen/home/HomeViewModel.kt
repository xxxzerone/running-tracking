package com.example.runningtracking.presentation.screen.home

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningtracking.data.service.LocationTrackerService
import com.example.runningtracking.domain.repository.LocationTrackerRepository
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
    private val locationTrackerRepository: LocationTrackerRepository
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
        _state.update { it.copy(isRunning = !isRunning) }

        val intent = Intent(context, LocationTrackerService::class.java).apply {
            action = if (isRunning) LocationTrackerService.ACTION_STOP else LocationTrackerService.ACTION_START
        }
        context.startForegroundService(intent)
    }

    private fun fetchLocation() {
        locationTrackerRepository.observeLocation()
            .onEach { location ->
                _state.update {
                    it.copy(location = LatLng(location.latitude, location.longitude))
                }
            }
            .catch { e ->
                // Handle errors, e.g. missing permissions propagated from tracker
                e.printStackTrace()
            }
            .launchIn(viewModelScope)
    }
}