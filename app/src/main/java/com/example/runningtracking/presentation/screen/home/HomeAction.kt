package com.example.runningtracking.presentation.screen.home

sealed interface HomeAction {
    data object OnFetchLocation : HomeAction
}