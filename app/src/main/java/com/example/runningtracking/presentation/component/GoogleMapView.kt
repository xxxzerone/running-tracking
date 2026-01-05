package com.example.runningtracking.presentation.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun GoogleMapView(
    location: LatLng?,
    modifier: Modifier = Modifier
) {
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(location) {
        location?.let {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(it, 17f)
            )
        }
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = location != null),
        uiSettings = MapUiSettings(zoomControlsEnabled = true)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun GoogleMapViewPreview() {
    GoogleMapView(
        location = LatLng(37.5503, 126.9971)
    )
}