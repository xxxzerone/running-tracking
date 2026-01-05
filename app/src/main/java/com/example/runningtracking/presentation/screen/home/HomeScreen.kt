package com.example.runningtracking.presentation.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.runningtracking.presentation.component.GoogleMapView
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun HomeScreen(
    state: HomeState,
    cameraPositionState: CameraPositionState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        GoogleMapView(
            cameraPositionState = cameraPositionState
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    val cameraPositionState = rememberCameraPositionState()

    HomeScreen(
        state = HomeState(),
        cameraPositionState = cameraPositionState
    )
}