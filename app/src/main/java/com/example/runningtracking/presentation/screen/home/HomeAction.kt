package com.example.runningtracking.presentation.screen.home

sealed interface HomeAction {
    data object OnFetchLocation : HomeAction
    data object OnToggleRunning : HomeAction
    data object OnToggleRunLogs : HomeAction
}