package com.example.runningtracking.presentation.screen.home

sealed interface HomeEvent {
    data object GpsDisabled : HomeEvent
}