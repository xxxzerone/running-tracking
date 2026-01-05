package com.example.runningtracking.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningtracking.domain.location.LocationTracker
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class HomeViewModel(
    private val locationTracker: LocationTracker
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.OnFetchLocation -> fetchLocation()
        }
    }

    private fun fetchLocation() {
        locationTracker.observeLocation()
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