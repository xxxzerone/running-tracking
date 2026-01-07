package com.example.runningtracking.presentation.screen.home

sealed interface HomeEvent {
    data object GpsDisabled : HomeEvent
    data object BatteryLow : HomeEvent
    data object RunFinishedLowBattery : HomeEvent
}